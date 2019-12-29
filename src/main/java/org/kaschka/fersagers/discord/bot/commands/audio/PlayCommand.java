package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertPermissions;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertVoiceChannelNotNull;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getCurrentVoiceChannel;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getMemberByName;

public class PlayCommand implements Command {

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        MessageUtils.logAndDeleteMessage(event);
        assertPlayCommand(args, event);
        assertPermissions("Bot Permissions", event.getMember());

        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = getCurrentVoiceChannel(event.getMember());
        assertVoiceChannelNotNull(event.getAuthor(), event.getAuthor().getName(), voiceChannel);

        if (audioManager.isConnected()) {
            MessageUtils.sendMessageToUser(event.getAuthor(),"Queueing Song.");
        } else {
            audioManager.openAudioConnection(voiceChannel);
        }

        PlayerManager manager = PlayerManager.getInstance();
        manager.loadAndPlay(getCurrentVoiceChannel(getMemberByName(event, event.getAuthor().getName())), args.get(0));
    }

    private void assertPlayCommand(List<String> args, MessageReceivedEvent event) {
        if (args.size() != 1) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Invalid args.\n Use /play [URL]");
            throw new RuntimeException();
        }
    }

    @Override
    public String getInvoke() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "/play [URL]: Plays the sound of the given url";
    }
}
