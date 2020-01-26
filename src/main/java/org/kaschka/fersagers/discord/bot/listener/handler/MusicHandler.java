package org.kaschka.fersagers.discord.bot.listener.handler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.validator.routines.UrlValidator;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class MusicHandler implements ChatHandler {

    private final static Logger logger = Logger.getInstance();

    private static final String MUSIC_CHANNEL = "musik";

    @Override
    public boolean handle(MessageReceivedEvent event) {
        if(event.getChannel().getName().equals(MUSIC_CHANNEL)) {
            String musicExceptionCase = event.getMessage().getContentRaw().split("\\s+")[0];

            if(!UrlValidator.getInstance().isValid(event.getMessage().getContentRaw()) && !musicExceptionCase.startsWith("!musik")) {
                event.getMessage().delete().queue();

                MessageUtils.sendMessageToUser(event.getAuthor(), String.format("Only URLs are allowed in the %s channel!", event.getChannel().getName()));
                logger.log(String.format("User %s@%s@%s wrote in %s Channel: %s",
                        event.getAuthor().getName(),
                        event.getGuild().getName(),
                        event.getChannel().getName(),
                        event.getChannel().getName(),
                        event.getMessage().getContentRaw()));
            }
            return true;
        }
        return false;
    }
}
