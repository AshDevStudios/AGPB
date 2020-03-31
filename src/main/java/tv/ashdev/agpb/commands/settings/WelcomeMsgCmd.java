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

/**
 * The type Welcome msg cmd.
 */
public class WelcomeMsgCmd extends Command {

  private final Agpb agpb;

  /**
   * Instantiates a new Welcome msg cmd.
   *
   * @param agpb the agpb
   */
  public WelcomeMsgCmd(Agpb agpb) {
    this.agpb = agpb;
    this.name = "welcomemsg";
    this.help = "Set welcome message for new users";
    this.arguments = "<MESSAGE or NONE>";
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

    if (event.getArgs().equalsIgnoreCase("none")) {
      agpb.getDatabase().getSettings().setWelcomeMsg(event.getGuild(), null);
      event.replySuccess("The welcome message command has been reset");
      return;
    }

    agpb.getDatabase().getSettings().setWelcomeMsg(event.getGuild(), event.getArgs());
    event.replySuccess("The new welcome message has been set!");

  }
}
