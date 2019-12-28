package org.kaschka.fersagers.discord.bot.commands;

import java.util.List;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertUserIsInChannel;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getCurrentVoiceChannel;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getMemberByName;

public class PlayCommand implements Command {

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        MessageUtils.logAndDeleteMessage(event);

        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = getCurrentVoiceChannel(event.getMember());
        assertUserIsInChannel(event.getAuthor(), event.getAuthor().getName(), voiceChannel);

        if (audioManager.isConnected()) {
            MessageUtils.sendMessageToUser(event.getAuthor(),"I'm already connected to a channel. Queueing Song");
        } else {
            audioManager.openAudioConnection(voiceChannel);
        }

        PlayerManager manager = PlayerManager.getInstance();
        manager.loadAndPlay(getCurrentVoiceChannel(getMemberByName(event, event.getAuthor().getName())), args.get(0));
    }

    @Override
    public String getInvoke() {
        return "play";
    }
}
