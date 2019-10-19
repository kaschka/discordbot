package org.kaschka.fersagers.discord.bot.utils;

import net.dv8tion.jda.api.entities.User;

public final class MessageUtils {

    private static Logger logger = Logger.getInstance();

    public static void sendMessageToUser(User user, String string) {
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(string).queue());
        logger.log(string);
    }
}
