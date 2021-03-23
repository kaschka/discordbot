package org.kaschka.fersagers.discord.bot.commands;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;

import java.util.List;

public interface Command {
    void handle(List<String> args, MessageReceivedEvent event);

    default boolean isDirectMessageEnabled() {
        return false;
    }

    String getInvoke();

    String getHelp();

    default Permissions requiredPermissions() {
        return Permissions.STANDARD_PERMISSIONS;
    }
}
