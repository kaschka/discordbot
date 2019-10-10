package org.kaschka.fersagers.discord.bot.utils;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatLogger {

    private Logger logger = LoggerFactory.getLogger("ChatLogger");

    public void logChatMessage(MessageReceivedEvent message) {
        logger.info(String.format("[%s@%s] %s: %s", message.getGuild().getName(), message.getChannel().getName(), message.getAuthor().getName(),
                message.getMessage()));
    }
}
