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

package tv.AshDev.AGPB.listeners;

import java.util.Objects;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.AshDev.AGPB.settings.BotSettings;
import tv.AshDev.AGPB.settings.GuildProperties;

/**
 * The type Guild event.
 */
public class GuildEvent extends ListenerAdapter {

  private static final Logger log = LoggerFactory.getLogger(GuildEvent.class);

  // The bot settings object initialized in the Main class
  private final BotSettings botSettings;

  /**
   * Constructor for the GuildEvent with the bot settings
   *
   * @param botSettings The bot settings form the Main class
   */
  public GuildEvent(final BotSettings botSettings) {
    this.botSettings = botSettings;
  }

  /**
   * On guild member join.
   *
   * @param event the event
   */
  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    // The guild ID
    final long guildId = event.getGuild().getIdLong();

    log.info("{} has joined: {}", event.getUser().getName(), event.getGuild().getName());
    User user = event.getUser();
    user.openPrivateChannel().queue(
        privateChannel -> {
          privateChannel.sendMessageFormat(
              botSettings.getSettings(guildId).getProperty(GuildProperties.WELCOME_MESSAGE))
              .queue();
          log.info("Private Message has been sent");
        }
    );
  }

  /**
   * On guild join.
   *
   * @param event the event
   */
  @Override
  public void onGuildJoin(GuildJoinEvent event) {
    JDA api = event.getJDA();
    long guildId = Objects.requireNonNull(api.getGuildById(event.getGuild().getId())).getIdLong();
    botSettings.createFile(guildId);
    botSettings.getSettings(guildId)
        .setProperty(GuildProperties.GUILD_NAME, event.getGuild().getName());
    botSettings.getSettings(guildId).save();
  }
}
