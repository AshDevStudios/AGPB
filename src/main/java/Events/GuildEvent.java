package Events;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuildEvent extends ListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(GuildEvent.class);

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        log.info("{} has joined: {}", event.getUser().getName(), event.getGuild().getName());
        User user = event.getUser();
        user.openPrivateChannel().queue(
                privateChannel -> {
                    privateChannel.sendMessageFormat("Message goes here!").queue();
                    log.info("Private Message has been sent");
                }
        );
    }

}
