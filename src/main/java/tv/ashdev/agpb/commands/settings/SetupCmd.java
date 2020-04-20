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
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.Permission;
import tv.ashdev.agpb.Agpb;
import tv.ashdev.agpb.Constants;

public class SetupCmd extends Command {

  private final Agpb agpb;
  private final EventWaiter waiter;

  public SetupCmd(Agpb agpb, EventWaiter waiter) {
    this.waiter = waiter;
    this.agpb = agpb;
    this.name = "setup";
    this.guildOnly = true;
    this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    this.category = Constants.CATEGORIES.get(1);
  }

  @Override
  protected void execute(CommandEvent event) {

  }
}
