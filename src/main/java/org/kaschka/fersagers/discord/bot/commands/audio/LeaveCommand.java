package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertPermissions;
import static org.kaschka.fersagers.discord.bot.utils.MessageUtils.logAndDeleteMessage;

public class LeaveCommand implements Command {
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        logAndDeleteMessage(event);
        assertPermissions("Bot Permissions", event.getMember());

        VoiceChannel connectedChannel = event.getGuild().getAudioManager().getConnectedChannel();
        assertConnectedChannel(connectedChannel, event.getAuthor());

        event.getGuild().getAudioManager().closeAudioConnection();
        PlayerManager.getInstance().stop(event.getGuild());
    }

    private void assertConnectedChannel(VoiceChannel connectedChannel, User user) {
        if(connectedChannel == null) {
            MessageUtils.sendMessageToUser(user, "Bot is not in a Channel!");
            throw new RuntimeException();
        }
    }

    @Override
    public String getInvoke() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "/leave: Stops the current track";
    }
}
