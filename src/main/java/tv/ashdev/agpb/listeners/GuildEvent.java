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

package tv.ashdev.agpb.listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.ashdev.agpb.Agpb;

/**
 * The type Guild event.
 */
public class GuildEvent extends ListenerAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(GuildEvent.class);

  private Agpb agpb;

  /**
   * Instantiates a new Guild event.
   *
   * @param agpb the agpb
   */
  public GuildEvent(Agpb agpb) {
    this.agpb = agpb;
  }

  /**
   * Constructor for the GuildEvent with the bot settings
   *
   * @param event the event
   */
  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    String msg = agpb.getDatabase().getSettings().getWelcomeMsg(event.getGuild());
    User user = event.getUser();
    user.openPrivateChannel().queue(
        privateChannel -> {
          privateChannel.sendMessageFormat(msg).queue();
        }
    );
  }
}
