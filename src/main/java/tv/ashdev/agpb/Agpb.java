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

package tv.ashdev.agpb;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.ashdev.agpb.commands.admin.ClipCmd;
import tv.ashdev.agpb.commands.global.GlobalSuggestionCmd;
import tv.ashdev.agpb.commands.settings.PrefixCmd;
import tv.ashdev.agpb.commands.settings.SettingsCmd;
import tv.ashdev.agpb.commands.settings.TimezoneCmd;
import tv.ashdev.agpb.commands.settings.WelcomeMsgCmd;
import tv.ashdev.agpb.data.BotToken;
import tv.ashdev.agpb.database.Database;
import tv.ashdev.agpb.listeners.GuildEvent;

/**
 * The type Main.
 */
public class Agpb {

  private static final Logger LOG = LoggerFactory.getLogger(Agpb.class);
  private final Database database;

  /**
   * Instantiates a new Main.
   *
   * @throws Exception the exception
   */
  public Agpb() throws Exception {

    BotToken botToken = new BotToken();
    database = new Database("localhost:3306/AGPB", "root", "root");

    EventWaiter waiter = new EventWaiter();
    CommandClientBuilder bot = new CommandClientBuilder()
        .setPrefix(Constants.PREFIX)
        .setStatus(OnlineStatus.ONLINE)
        .setActivity(Activity.watching("everyone"))
        .setOwnerId(Constants.OWNER_ID)
        .setEmojis(Constants.SUCCESS, Constants.WARNING, Constants.ERROR)
        .setServerInvite(Constants.SERVER_INVITE)
        .setGuildSettingsManager(database.settings)
        .addCommands(
            // GLOBAL COMMANDS
            new GlobalSuggestionCmd(this),
            // ADMIN COMMANDS
            new ClipCmd(this, waiter),
            //SETTINGS COMMANDS
            new PrefixCmd(this),
            new SettingsCmd(this),
            new TimezoneCmd(this),
            new WelcomeMsgCmd(this)

            // MODERATOR COMMANDS
        );

    JDA api = JDABuilder.create(Constants.GATEWAY_INTENTS)
        .setToken(botToken.getToken())
        .addEventListeners(waiter, bot.build(), new GuildEvent(this))
        .build();
    api.awaitReady();
    LOG.info("Finished Building JDA!");

  }

  /**
   * Gets database.
   *
   * @return the database
   */
  public Database getDatabase() {
    return database;
  }

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {
    // Instantiating the bot settings here so it can be used on every class you need
    new Agpb();
  }

}
