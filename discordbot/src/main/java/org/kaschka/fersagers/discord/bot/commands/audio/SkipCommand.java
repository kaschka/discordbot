package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getBotAndUserVoiceChannel;
import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.isInVoiceChannel;

public class SkipCommand implements Command {

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        VoiceChannel connectedChannel = getBotAndUserVoiceChannel(event.getMember());

        if(!isInVoiceChannel(event.getMember(), connectedChannel)) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "You are not in the same voicechannel as the Bot!");
        } else {
            PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.stopTrack();
        }
    }

    @Override
    public String getInvoke() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "/skip: Skips the current track";
    }

    @Override
    public Permissions requiredPermissions() {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.BOT_PERMISSIONS);
        return permissions;
    }
}