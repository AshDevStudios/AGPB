/*
 * MIT License
 *
 * Copyright (c) 2020 AshDev (Ashley Tonkin)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package tv.ashdev.agpb.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import java.time.OffsetDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.ashdev.agpb.Agpb;
import tv.ashdev.agpb.utils.TwitchConnection;

public class ClipCmd extends Command {

  private static final Logger LOG = LoggerFactory.getLogger(ClipCmd.class);
  private final Agpb agpb;
  private final EventWaiter waiter;
  private final TwitchConnection twitchConn = new TwitchConnection();
  private final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);


  public ClipCmd(Agpb agpb, EventWaiter waiter) {
    this.agpb = agpb;
    this.waiter = waiter;
    this.name = "clips";
    this.help = "Setup your Twitch clips";
    this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
  }


  @Override
  protected void execute(CommandEvent event) {
    event.replySuccess(
        "Let me help you set up the clips + " + event.getAuthor().getName()
            + ", You can type `cancel` at any time to stop the setup."
            + "\n What is the channel id you'd like me to post clips into?"
    );
    awaitChannelID(event);
  }


  public void awaitChannelID(CommandEvent event) {
    waiter.waitForEvent(GuildMessageReceivedEvent.class,
        e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()),
        e -> {
          if (e.getMessage().getContentRaw().equalsIgnoreCase("cancel")) {
            event.replyWarning("You have canceled the clip setup!");
            return;
          }
          String channelId = e.getMessage().getContentRaw().trim();
          event.replySuccess("Thank you " + event.getAuthor().getName() + "."
              + "\n What is the stream team name, you can get this from the URL eg `twitch.tv/team/**wgd**`."
              + "Its the last word in the URL.");
          awaitTwitchTeamName(event, channelId);

        }, 1, TimeUnit.MINUTES, () -> event.replyError("Sorry!, you took to long to reply"));
  }

  public void awaitTwitchTeamName(CommandEvent event, String channelId) {
    waiter.waitForEvent(GuildMessageReceivedEvent.class,
        e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()),
        e -> {
          if (e.getMessage().getContentRaw().equalsIgnoreCase("cancel")) {
            event.replyWarning("You have canceled the clip setup!");
            return;
          }
          String team = e.getMessage().getContentRaw().toLowerCase().trim();

          event.replySuccess("Thank you " + event.getAuthor().getName() + "."
              + "Adding your information to the database now.");
          addToDatabase(event, channelId, team);
        }, 1, TimeUnit.MINUTES, () -> event.replyError("Sorry!, you took to long to reply"));
  }


  public void addToDatabase(CommandEvent event, String channelId, String team) {
    agpb.getDatabase().settings.setClipsChannel(event.getGuild(), channelId);
    agpb.getDatabase().settings.setStreamTeam(event.getGuild(), team);
    event.replySuccess(
        "Your clips have been setup. You should see them coming thought now if there where any clips taken in the last 24 hours");
    LOG.info("Clip information added to database.");
    getClips(event);
  }


  public void getClips(CommandEvent event) {
    Runnable twitchClips = () -> {
      String getTeam = agpb.getDatabase().settings.getStreamTeam(event.getGuild());
      String getChannelId = agpb.getDatabase().settings.getClipsChannel(event.getGuild());
      String teamData = twitchConn.Connection("teams/" + getTeam);
      GuildChannel channel = event.getGuild().getGuildChannelById(ChannelType.TEXT, getChannelId);
      try {
        JSONObject team = new JSONObject(teamData);
        JSONArray teamArray = team.getJSONArray("users");

        for (int x = 0; x < teamArray.length(); x++) {
          JSONObject teamInfo = teamArray.getJSONObject(x);
          String clipData = twitchConn.ClipData("clips/top", teamInfo.getString("name"));
          JSONObject clips = new JSONObject(clipData);
          JSONArray clipsArray = clips.getJSONArray("clips");

          for (int i = 0; i < clipsArray.length(); i++) {
            JSONObject clip = clipsArray.getJSONObject(i);
            JSONObject broadcaster = clip.getJSONObject("broadcaster");
            JSONObject curator = clip.getJSONObject("curator");
            JSONObject thumbnails = clip.getJSONObject("thumbnails");

            EmbedBuilder builder = new EmbedBuilder()
                .setColor(event.getSelfMember().getColor())
                .setAuthor(team.getString("display_name") + " Clips", null,
                    event.getJDA().getSelfUser().getAvatarUrl())
                .setTimestamp(OffsetDateTime.parse(clip.getString("created_at")))
                .setTitle(broadcaster.getString("display_name"),
                    broadcaster.getString("channel_url"))
                .setThumbnail(broadcaster.getString("logo"))
                .addField("Clip Title", clip.getString("title"), false)
                .addField("Game", clip.getString("game"), false)
                .addField("Clip", "https://clips.twitch.tv/" + clip.getString("slug"), false)
                .setImage(thumbnails.getString("medium"))
                .setFooter(curator.getString("display_name"), curator.getString("logo"));
            TextChannel textChannel =
                channel != null ? channel.getGuild().getTextChannelById(getChannelId) : null;
            if (textChannel != null) {
              textChannel.sendMessage(builder.build()).queue();
            }
          }
        }
      } catch (JSONException e) {
        LOG.error("Something went wrong! Please double check code");
        e.printStackTrace();
      }
    };
    scheduled.scheduleWithFixedDelay(twitchClips, 0, 24, TimeUnit.HOURS);
    LOG.info("Getting clips now.");
  }
}
