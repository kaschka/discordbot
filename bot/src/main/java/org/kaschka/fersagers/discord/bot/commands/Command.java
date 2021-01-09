package org.kaschka.fersagers.discord.bot.commands;


import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;

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
