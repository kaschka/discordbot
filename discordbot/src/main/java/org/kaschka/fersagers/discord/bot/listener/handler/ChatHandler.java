package org.kaschka.fersagers.discord.bot.listener.handler;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ChatHandler {
    /**
     *
     * @param event
     * @return true when the handler was responsible for the message
     */
    boolean handle(MessageReceivedEvent event);
    default void handleOnStartup(List<Guild> guilds) {};
}
