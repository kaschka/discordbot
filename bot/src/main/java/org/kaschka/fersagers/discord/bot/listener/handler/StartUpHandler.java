package org.kaschka.fersagers.discord.bot.listener.handler;

import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public interface StartUpHandler {
    default void handleOnStartup(List<Guild> guilds) {
    }

    default void handleOnStartup() {
    }
}
