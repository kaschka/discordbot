package org.kaschka.fersagers.discord.bot.utils;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DiscordUtils {

    public static VoiceChannel getCurrentVoiceChannel(Member member) {
        return member.getVoiceState().getChannel();
    }

    public static void assertVoiceChannelNotNull(User author, String memberName, VoiceChannel voiceChannel) throws RuntimeException {
        if (voiceChannel == null) {
            MessageUtils.sendMessageToUser(author, String.format("User '%s' is not in a voicechannel", memberName));
            throw new RuntimeException();
        }
    }

    public static Member getMemberByName(MessageReceivedEvent event, String name) throws RuntimeException {
        List<Member> membersByName = event.getGuild().getMembersByName(name, true);
        if (membersByName.isEmpty()) {
            MessageUtils.sendMessageToUser(event.getAuthor(), String.format("User '%s' does not exist", name));
            throw new RuntimeException();
        }
        return membersByName.get(0);
    }

    public static boolean isInVoiceChannel(Member member, VoiceChannel channel) {
        return channel.getMembers().contains(member);
    }

    public static VoiceChannel getBotAndUserVoiceChannel(Member member) throws RuntimeException {
        VoiceChannel connectedChannelBot = member.getGuild().getAudioManager().getConnectedChannel();
        VoiceChannel connectedChannelMember = getCurrentVoiceChannel(member);

        if (connectedChannelMember == null || connectedChannelBot != connectedChannelMember) {
            MessageUtils.sendMessageToUser(member.getUser(), "You and the Bot are not in the same voicechannel!");
            throw new RuntimeException("Command was run while Bot was not in a Channel");
        }
        return connectedChannelBot;
    }

}
