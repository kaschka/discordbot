package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertPermissions;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.isInVoiceChannel;

public class SkipCommand implements Command {
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        MessageUtils.logAndDeleteMessage(event);
        assertPermissions("Bot Permissions", event.getMember());

        VoiceChannel connectedChannel = event.getGuild().getAudioManager().getConnectedChannel();
        assertConnectedChannel(connectedChannel, event.getAuthor());

        if(!isInVoiceChannel(event.getMember(), connectedChannel)) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "You are not in the same voicechannel as the Bot!");
        } else {
            PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.stopTrack();
        }
    }

    private void assertConnectedChannel(VoiceChannel connectedChannel, User user) {
        if(connectedChannel == null) {
            MessageUtils.sendMessageToUser(user, "Bot is not in a Channel!");
            throw new RuntimeException();
        }
    }

    @Override
    public String getInvoke() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "/skip: Skips the current track";
    }
}
