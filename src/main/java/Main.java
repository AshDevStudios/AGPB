import Config.BotToken;
import Events.GuildEvent;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Main {
    private static BotToken botToken = new BotToken();
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoginException, InterruptedException {

        EventWaiter waiter = new EventWaiter();
        CommandClientBuilder bot = new CommandClientBuilder()
                .setPrefix("-")
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("everyone"))
                .setOwnerId("160269653136900096");

        JDA api = JDABuilder.createDefault(botToken.getToken())
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("testing"))
                .setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_EMOJIS)
                .addEventListeners(waiter, bot.build(), new GuildEvent())
                .build();
        api.awaitReady();
        log.info("Finished Building JDA!");

    }
}
