package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiredPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;

import static org.kaschka.fersagers.discord.bot.utils.DiscordUtils.getBotAndUserVoiceChannel;

public class LeaveCommand implements Command {

    @Override
    @RequiredPermission(Role.BOT_PERMISSIONS)
    public void handle(List<String> args, MessageReceivedEvent event) {
        getBotAndUserVoiceChannel(event.getMember());

        event.getGuild().getAudioManager().closeAudioConnection();
        PlayerManager.getInstance().stop(event.getGuild());
    }

    @Override
    public String getInvoke() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "/leave: Stops the current track";
    }
}
