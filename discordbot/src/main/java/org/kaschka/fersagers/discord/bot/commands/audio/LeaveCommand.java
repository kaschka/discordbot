package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.AudioPlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.InMemoryConfiguration;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getBotAndUserVoiceChannel;

public class LeaveCommand implements Command {

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        getBotAndUserVoiceChannel(event.getMember());
        AudioPlayerManager.getInstance().stop(event.getGuild());
        InMemoryConfiguration.isManuallyJoined.remove(event.getGuild().getIdLong());
    }

    @Override
    public String getInvoke() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "/leave: Stops the current track";
    }

    @Override
    public Permissions requiredPermissions() {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.BOT_PERMISSIONS);
        return permissions;
    }
}
