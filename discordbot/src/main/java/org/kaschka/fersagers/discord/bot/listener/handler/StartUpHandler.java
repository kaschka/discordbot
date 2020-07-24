package org.kaschka.fersagers.discord.bot.listener.handler;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;

public interface StartUpHandler {
    default void handleOnStartup(List<Guild> guilds) {};
    default void handleOnStartup() {};
}
