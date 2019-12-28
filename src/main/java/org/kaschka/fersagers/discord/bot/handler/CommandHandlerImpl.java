package org.kaschka.fersagers.discord.bot.handler;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.commons.lang3.StringUtils;
import org.kaschka.fersagers.discord.bot.soundplayer.PlayerManager;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class CommandHandlerImpl implements CommandHandler {

    private final static Logger logger = Logger.getInstance();

    public boolean handle(String message, MessageReceivedEvent event) {
        if(StringUtils.isEmpty(message)) {
            return false;
        }

        String[] messages = message.split("\\s+");
        String command = messages[0];
        String[] args = Arrays.copyOfRange(messages, 1, messages.length);

        if(command.equals("/fuck")) {
            handleFuck(args, event);
            return true;
        }
        if(command.equals("/play")) {
            return handlePlay(args, event);
        }
        if(command.equals("/leave")) {
            event.getGuild().getAudioManager().closeAudioConnection();
            PlayerManager.getInstance().stop(event.getGuild());
            return true;
        }

        return false;
    }

    private boolean handlePlay(String[] args, MessageReceivedEvent event) {
        logger.logChatMessage(event);
        event.getMessage().delete().queue();

        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel voiceChannel = getCurrentVoiceChannel(event.getMember());
        assertUserIsInChannel(event.getAuthor(), event.getAuthor().getName(), voiceChannel);

        if (audioManager.isConnected()) {
            MessageUtils.sendMessageToUser(event.getAuthor(),"I'm already connected to a channel. Queueing Song");
        } else {
            audioManager.openAudioConnection(voiceChannel);
        }

        PlayerManager manager = PlayerManager.getInstance();
        manager.loadAndPlay(getCurrentVoiceChannel(getMemberByName(event, event.getAuthor().getName())), args[0]);
        manager.getGuildMusicManager(event.getGuild()).player.setVolume(100);
        return true;
    }

    private void handleFuck(String[] args, MessageReceivedEvent event) {
        logger.logChatMessage(event);
        event.getMessage().delete().queue();
        assertFuckCommand(args, event);

        Guild guild = event.getGuild();
        String memberName = args[0];
        String channelName = args[1];

        Member member = getMemberByName(event, memberName);

        VoiceChannel channelBefore = getCurrentVoiceChannel(member);
        assertUserIsInChannel(event.getAuthor(), memberName, channelBefore);

        VoiceChannel newChannel = guild
                .createVoiceChannel(channelName)
                .setParent(channelBefore.getParent())
                .complete();

        for (int i = 0; i < 5; i++) {
            guild.moveVoiceMember(member, newChannel).completeAfter(500, TimeUnit.MILLISECONDS);
            guild.moveVoiceMember(member, channelBefore).completeAfter(500, TimeUnit.MILLISECONDS);
        }

        newChannel.delete().queue();
    }

    private Member getMemberByName(MessageReceivedEvent event, String name) {
        List<Member> membersByName = event.getGuild().getMembersByName(name, true);
        if(membersByName.isEmpty()) {
            MessageUtils.sendMessageToUser(event.getAuthor(), String.format("User '%s' does not exist", name));

            throw new RuntimeException();
        }
        return membersByName.get(0);
    }

    private VoiceChannel getCurrentVoiceChannel(Member member) {
        return member.getVoiceState().getChannel();
    }

    private boolean hasPermissions(String requiredRole, Member member) {
        if(member == null || requiredRole == null) {
            throw new RuntimeException();
        }

        List<Role> roles = member.getRoles();
        for(Role role : roles) {
            if(role.getName().equals(requiredRole)) {
                return true;
            }
        }
        return false;
    }

    private void assertFuckCommand(String[] args, MessageReceivedEvent event) {
        if (!hasPermissions("Bot Permissions", event.getMember())) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Required permissions are missing!");
            throw new RuntimeException();
        } else if (args.length != 2) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Invalid args.\n Use /fuck [Name] [Name]");
            throw new RuntimeException();
        }
    }

    private void assertUserIsInChannel(User author, String memberName, VoiceChannel voiceChannel) {
        if (voiceChannel == null) {
            MessageUtils.sendMessageToUser(author, String.format("User '%s' is not in a voicechannel", memberName));
            throw new RuntimeException();
        }
    }
}
