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

package tv.ashdev.agpb.commands.settings;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import tv.ashdev.agpb.Agpb;
import tv.ashdev.agpb.Constants;
import tv.ashdev.agpb.utils.FormatUtil;

/**
 * The type Settings cmd.
 */
public class SettingsCmd extends Command {

  private final Agpb agpb;

  /**
   * Instantiates a new Settings cmd.
   *
   * @param agpb the agpb
   */
  public SettingsCmd(Agpb agpb) {
    this.agpb = agpb;
    this.name = "settings";
    this.help = "Shows current settings for your server";
    this.guildOnly = true;
    this.category = Constants.CATEGORIES.get(3);
    this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
  }

  /**
   * Execute.
   *
   * @param event the event
   */
  @Override
  protected void execute(CommandEvent event) {

    event.getChannel().sendMessage(new MessageBuilder()
        .append(FormatUtil.filterEveryone(
            "**" + event.getSelfUser().getName() + "** settings on **" + event.getGuild().getName()
                + "**:"))
        .setEmbed(new EmbedBuilder()
            .addField(agpb.getDatabase().settings.getSettingsDisplay(event.getGuild()))
            .setColor(event.getSelfMember().getColor())
            .build()).build()).queue();

  }
}
