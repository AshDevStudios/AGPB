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

package tv.ashdev.agpb.commands.global;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import net.dv8tion.jda.api.EmbedBuilder;
import tv.ashdev.agpb.Agpb;
import tv.ashdev.agpb.Constants;

public class SuggestionCmd extends Command {

  private Agpb agpb;

  public SuggestionCmd(Agpb agpb) {
    this.agpb = agpb;
    this.name = "gsuggestion";
    this.aliases = new String[]{"gs", "gsuggest"};
    this.arguments = "<SUGGESTION>";
    this.category = Constants.CATEGORIES.get(0);
    this.help = "Send suggestions straight to the AGPB discord";
  }

  @Override
  protected void execute(CommandEvent event) {
    String guildID = "694486487135289445";
    String textChannelID = "694537948062023753";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a dd/MM/yy");
    ZoneId zone = agpb.getDatabase().getSettings().getTimeZone(event.getGuild());

    EmbedBuilder eb = new EmbedBuilder()
        .addField("Suggestion", event.getArgs(), false)
        .setAuthor(event.getMember().getUser().getName(), null,
            event.getMember().getUser().getAvatarUrl())
        .setFooter(event.getGuild().getName() + " - " + event.getMessage().getTimeCreated()
            .format(formatter.withZone(zone)));

    Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(guildID))
        .getTextChannelById(textChannelID)).sendMessage(eb.build()).queue(message -> {
      event.getMessage().delete().queue();
    });
  }
}
