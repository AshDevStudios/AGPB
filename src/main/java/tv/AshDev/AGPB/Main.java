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

package tv.AshDev.AGPB;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.AshDev.AGPB.commands.SetWelcomeMessage;
import tv.AshDev.AGPB.data.BotToken;
import tv.AshDev.AGPB.listeners.GuildEvent;
import tv.AshDev.AGPB.settings.BotSettings;

/**
 * The type Main.
 */
public class Main {

  private static final BotToken botToken = new BotToken();
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws LoginException       the login exception
   * @throws InterruptedException the interrupted exception
   */
  public static void main(String[] args) throws LoginException, InterruptedException {
    // Instantiating the bot settings here so it can be used on every class you need
    final BotSettings botSettings = new BotSettings();

    EventWaiter waiter = new EventWaiter();
    CommandClientBuilder bot = new CommandClientBuilder()
        .setPrefix(Constants.PREFIX)
        .setStatus(OnlineStatus.ONLINE)
        .setActivity(Activity.watching("everyone"))
        .setOwnerId(Constants.OWNER_ID)
        .setEmojis(Constants.SUCCESS, Constants.WARNING, Constants.ERROR)
        .setServerInvite(Constants.SERVER_INVITE)
        .addCommands(
            new SetWelcomeMessage(botSettings)
        );

    JDA api = JDABuilder.create(Constants.GATEWAY_INTENTS)
        .setToken(botToken.getToken())
        .addEventListeners(waiter, bot.build(), new GuildEvent(botSettings))
        .build();
    api.awaitReady();
    LOG.info("Finished Building JDA!");

  }

}
