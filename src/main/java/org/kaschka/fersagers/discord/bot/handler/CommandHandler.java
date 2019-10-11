package org.kaschka.fersagers.discord.bot.handler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandHandler {
    boolean handle(String message, MessageReceivedEvent event);
}
