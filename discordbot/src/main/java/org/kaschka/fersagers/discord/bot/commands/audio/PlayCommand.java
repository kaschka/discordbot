package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.commons.validator.routines.UrlValidator;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertVoiceChannelNotNull;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getCurrentVoiceChannel;

public class PlayCommand implements Command {

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        assertPlayCommand(args, event);
        if(!UrlValidator.getInstance().isValid(args.get(0))) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Only URLs are allowed!");
            return;
        }
        playSound(event, args.get(0));

    }

    public static void playSound(MessageReceivedEvent event, String url) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = getCurrentVoiceChannel(event.getMember());
        assertVoiceChannelNotNull(event.getAuthor(), event.getAuthor().getName(), voiceChannel);

        if (audioManager.isConnected()) {
            MessageUtils.sendMessageToUser(event.getAuthor(),"Queueing Song.");
        } else {
            audioManager.openAudioConnection(voiceChannel);
        }

        putInQueue(url, event.getMember());
    }

    private static void putInQueue(String url, Member member) {
            try {
                PlayerManager.getInstance().loadAndPlay(getCurrentVoiceChannel(member), url);
            } catch (RuntimeException e) {
                MessageUtils.sendMessageToUser(member.getUser(), "Queue is full! Please delete Tracks or wait.");
                throw e;
            }
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

    @Override
    public Permissions requiredPermissions() {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.BOT_PERMISSIONS);
        return permissions;
    }
}
