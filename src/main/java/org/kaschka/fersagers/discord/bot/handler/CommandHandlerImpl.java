package org.kaschka.fersagers.discord.bot.handler;


import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

public class CommandHandlerImpl implements CommandHandler {

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

        return false;
    }

    private void handleFuck(String[] args, MessageReceivedEvent event) {
        event.getMessage().delete().complete();
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

        try {
            Thread.sleep(500);
            for (int i = 0; i < 5; i++) {
                Thread.sleep(500);
                guild.moveVoiceMember(member, newChannel).complete();
                Thread.sleep(500);
                guild.moveVoiceMember(member, channelBefore).complete();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            newChannel.delete().queue();
        }
    }

    private Member getMemberByName(MessageReceivedEvent event, String name) {
        List<Member> membersByName = event.getGuild().getMembersByName(name, true);
        if(membersByName.isEmpty()) {
            event.getAuthor().openPrivateChannel().queue((channel) -> channel.sendMessage(String.format("User '%s' does not exist", name)).queue());
            throw new RuntimeException();
        }
        return membersByName.get(0);
    }

    private VoiceChannel getCurrentVoiceChannel(Member member) {
        Guild guild = member.getGuild();
        List<VoiceChannel> voiceChannels = guild.getVoiceChannels();

        for (VoiceChannel voiceChannel : voiceChannels) {
            if(voiceChannel.getMembers().contains(member)) {
                return voiceChannel;
            }
        }
        return null;
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
            event.getAuthor().openPrivateChannel().queue((channel) -> channel.sendMessage("No permissions").queue());
            throw new RuntimeException();
        } else if (args.length != 2) {
            event.getAuthor().openPrivateChannel().queue((channel) -> channel.sendMessage("Invalid args.\n Use /fuck [Name] [Name]").queue());
            throw new RuntimeException();
        }
    }

    private void assertUserIsInChannel(User author, String memberName, VoiceChannel voiceChannel) {
        if (voiceChannel == null) {
            author.openPrivateChannel().queue((channel) -> channel.sendMessage(String.format("User '%s' is not in a voicechannel", memberName)).queue());
            throw new RuntimeException();
        }
    }
}
