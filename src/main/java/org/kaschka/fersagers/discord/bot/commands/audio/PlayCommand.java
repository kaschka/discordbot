package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.commons.validator.routines.UrlValidator;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiredPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertVoiceChannelNotNull;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getCurrentVoiceChannel;

public class PlayCommand implements Command {

    @Override
    @RequiredPermission(Role.BOT_PERMISSIONS)
    public void handle(List<String> args, MessageReceivedEvent event) {
        MessageUtils.logAndDeleteMessage(event);
        assertPlayCommand(args, event);

        if(!UrlValidator.getInstance().isValid(args.get(0))) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Only URLs are allowed!");
            return;
        }

        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = getCurrentVoiceChannel(event.getMember());
        assertVoiceChannelNotNull(event.getAuthor(), event.getAuthor().getName(), voiceChannel);

        if (audioManager.isConnected()) {
            MessageUtils.sendMessageToUser(event.getAuthor(),"Queueing Song.");
        } else {
            audioManager.openAudioConnection(voiceChannel);
        }

        putInQueue(args, event.getMember());
    }

    private void putInQueue(List<String> args, Member member) {
        int loops = 1;
        if(args.size() == 2) {
            int integer = Integer.parseInt(args.get(1));
            loops = integer <= 0 || integer >= 100 ? 1 : integer;
        }
        for (int i = 0; i <= loops; i++) {
            PlayerManager.getInstance().loadAndPlay(getCurrentVoiceChannel(member), args.get(0));
        }
    }

    private void assertPlayCommand(List<String> args, MessageReceivedEvent event) {
        if (args.size() < 1) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Invalid args.\n Use /play [URL] [Number between 1 and 100]");
            throw new RuntimeException();
        }
    }

    @Override
    public String getInvoke() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "/play [URL] [Number between 1 and 100]: Plays the sound of the given url";
    }
}
