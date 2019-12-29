package org.kaschka.fersagers.discord.bot.utils;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class MessageUtils {

    private static Logger logger = Logger.getInstance();

    public static void sendMessageToUser(User user, String string) {
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(string).queue());
        logger.log(string);
    }

    public static void logAndDeleteMessage(MessageReceivedEvent event) {
        if(!event.isFromType(ChannelType.PRIVATE)) {
            event.getMessage().delete().queue();
        }
        logger.logChatMessage(event);
    }
}
