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

    public static void assertUserIsInChannel(User author, String memberName, VoiceChannel voiceChannel) {
        if (voiceChannel == null) {
            MessageUtils.sendMessageToUser(author, String.format("User '%s' is not in a voicechannel", memberName));
            throw new RuntimeException();
        }
    }

    public static Member getMemberByName(MessageReceivedEvent event, String name) {
        List<Member> membersByName = event.getGuild().getMembersByName(name, true);
        if(membersByName.isEmpty()) {
            MessageUtils.sendMessageToUser(event.getAuthor(), String.format("User '%s' does not exist", name));
            throw new RuntimeException();
        }
        return membersByName.get(0);
    }

    public static boolean hasPermissions(String requiredRole, Member member) {
        if(member == null || requiredRole == null) {
            throw new RuntimeException();
        }
        return member.getRoles().stream().anyMatch(role -> role.getName().equals(requiredRole));
    }
}
