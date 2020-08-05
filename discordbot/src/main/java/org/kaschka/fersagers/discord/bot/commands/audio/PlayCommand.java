package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.commons.validator.routines.UrlValidator;
import org.kaschka.fersagers.discord.bot.audio.AudioPlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.InMemoryConfiguration;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertVoiceChannelNotNull;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getCurrentVoiceChannel;

public class PlayCommand implements Command {

    private static final int URL_ARG_NUMBER = 0;
    private static final int AMOUNT_ARG_NUMBER = 1;


    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        assertPlayCommand(args, event);
        if(!UrlValidator.getInstance().isValid(args.get(URL_ARG_NUMBER))) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Only URLs are allowed!");
            return;
        }
        int amount = 0;
        try {
            if (args.size() >= 2) {
                amount = Integer.parseInt(args.get(AMOUNT_ARG_NUMBER));
            }
        } catch (Exception e) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Please provide a valid Number!");
        }
        playSound(event, args.get(URL_ARG_NUMBER), amount);

    }

    public static void playSound(MessageReceivedEvent event, String url, int amount) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = getCurrentVoiceChannel(event.getMember());
        assertVoiceChannelNotNull(event.getAuthor(), event.getAuthor().getName(), voiceChannel);

        if (audioManager.isConnected()) {
            MessageUtils.sendMessageToUser(event.getAuthor(),"Queueing Song.");
        } else {
            audioManager.openAudioConnection(voiceChannel);
            InMemoryConfiguration.isManuallyJoined.put(event.getGuild().getIdLong(), false);
        }

        putInQueue(url, event.getMember(), amount);
    }

    private static void putInQueue(String url, Member member, int amount) {
        if(amount <= 0) {
            amount = 1;
        }

        try {
            VoiceChannel currentVoiceChannel = getCurrentVoiceChannel(member);
            for (int i = 0; i < amount; i++) {
                AudioPlayerManager.getInstance().loadAndPlay(currentVoiceChannel, url);
            }
        } catch (RuntimeException e) {
            MessageUtils.sendMessageToUser(member.getUser(), "Queue is full! Please delete Tracks or wait.");
            throw e;
        }
    }

    private void assertPlayCommand(List<String> args, MessageReceivedEvent event) {
        if (args.size() < 1 ||args.size() > 2) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Invalid args.\n Use /play [URL] [Times]");
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
