package org.kaschka.fersagers.discord.bot.listener.handler;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.validator.routines.UrlValidator;
import org.kaschka.fersagers.discord.bot.utils.Logger;

public class AramHandler implements ChatHandler {

    private final static Logger logger = Logger.getInstance();

    private final static long CHANNEL_ID_ARAM_KUECHE = 837382339025305670L;
    private final static long USER_ID_ARAM = 138023894505357312L;

    @Override
    public boolean handle(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if (isUrlByAramInAramKueche(message)) {
            thankAram(message.getChannel());
            return true;
        }
        return false;
    }

    private void thankAram(MessageChannel channel) {
        channel.sendMessage("Danke Aram! Wir lieben dich!").queue();

        logger.log("Thanked Aram!");
    }

    private boolean isUrlByAramInAramKueche(Message message) {
        return  message.getAuthor().getIdLong() == USER_ID_ARAM &&
                message.getChannel().getIdLong() == CHANNEL_ID_ARAM_KUECHE &&
                message.getContentRaw().startsWith("http") &&
                UrlValidator.getInstance().isValid(message.getContentRaw());
    }
}
