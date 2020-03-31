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
import net.dv8tion.jda.api.Permission;
import tv.ashdev.agpb.Agpb;
import tv.ashdev.agpb.Constants;
import tv.ashdev.agpb.database.GuildSettingsDataManager;

/**
 * The type Set welcome message.
 */
public class PrefixCmd extends Command {

  private final Agpb agpb;


  /**
   * Instantiates a new Set welcome message.
   *
   * @param agpb the agpb
   */
  public PrefixCmd(Agpb agpb) {
    this.agpb = agpb;
    this.name = "prefix";
    this.help = "Set a custom server prefix.";
    this.guildOnly = true;
    this.arguments = "<PREFIX or NONE>";
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
    if (event.getArgs().isEmpty()) {
      event.replyError(
          "Please include a prefix. The server's current prefix can be seen via the `" + event
              .getClient().getPrefix() + "settings` command");
      return;
    }

    if (event.getArgs().equalsIgnoreCase("none")) {
      agpb.getDatabase().getSettings().setPrefix(event.getGuild(), null);
      event.replySuccess("The server prefix has been reset.");
      return;
    }

    if (event.getArgs().length() > GuildSettingsDataManager.PREFIX_MAX_LENGTH) {
      event.replySuccess(
          "Prefixes cannot be longer than `" + GuildSettingsDataManager.PREFIX_MAX_LENGTH
              + "` characters.");
      return;
    }

    agpb.getDatabase().getSettings().setPrefix(event.getGuild(), event.getArgs());
    event.replySuccess("The server prefix has been set to `" + event.getArgs() + "`\n"
        + "Note that the default prefix (`"
        + event.getClient().getPrefix()
        + "`) cannot be removed and will word in addition to the custom prefix");

  }

}
