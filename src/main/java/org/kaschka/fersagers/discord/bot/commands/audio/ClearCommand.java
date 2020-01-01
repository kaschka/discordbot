package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiredPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class ClearCommand implements Command {

    @Override
    @RequiredPermission(value = Role.NO_ROLE, allowedIds = {407250792756674561L, 138025874384486400L})
    public void handle(List<String> args, MessageReceivedEvent event) {
        PlayerManager.getInstance().clear();
        MessageUtils.sendMessageToUser(event.getAuthor(), "Cleared Track Queue!");
    }

    @Override
    public boolean isDirectMessageEnabled() {
        return true;
    }

    @Override
    public String getInvoke() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return "/clear: Clears the Track Queues of all Servers";
    }
}
