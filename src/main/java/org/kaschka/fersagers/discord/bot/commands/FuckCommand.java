package org.kaschka.fersagers.discord.bot.commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.assertVoiceChannelNotNull;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getCurrentVoiceChannel;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getMemberByName;

public class FuckCommand implements Command {

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        assertFuckCommand(args, event);

        Guild guild = event.getGuild();
        String memberName = args.get(0);
        String channelName = args.get(1);
        Member member = getMemberByName(event, memberName);

        VoiceChannel channelBefore = getCurrentVoiceChannel(member);
        assertVoiceChannelNotNull(event.getAuthor(), memberName, channelBefore);
        VoiceChannel newChannel = getChannel(guild, channelBefore.getParent(), channelName);

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                guild.moveVoiceMember(member, newChannel).completeAfter(500, TimeUnit.MILLISECONDS);
                guild.moveVoiceMember(member, channelBefore).completeAfter(500, TimeUnit.MILLISECONDS);
            }
        }).start();
    }

    private VoiceChannel getChannel(Guild guild, Category parent, String name) {
        List<VoiceChannel> voiceChannelsByName = guild.getVoiceChannelsByName(name, true);
        VoiceChannel newVoiceChannel;

        if (!voiceChannelsByName.isEmpty()) {
            newVoiceChannel = voiceChannelsByName.get(0);
        } else {
            newVoiceChannel = guild
                    .createVoiceChannel(name)
                    .setParent(parent)
                    .complete();
            newVoiceChannel.delete().queueAfter(20, TimeUnit.SECONDS);
        }
        return newVoiceChannel;
    }

    private void assertFuckCommand(List<String> args, MessageReceivedEvent event) {
        if (args.size() != 2) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Invalid args.\n Use /fuck [Name] [Name]");
            throw new RuntimeException();
        }
    }

    @Override
    public String getInvoke() {
        return "fuck";
    }

    @Override
    public String getHelp() {
        return "/fuck [Nickname] [ChannelName]: User fucks the given Channel";
    }

    @Override
    public Permissions requiredPermissions() {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.BOT_PERMISSIONS);
        return permissions;
    }
}
