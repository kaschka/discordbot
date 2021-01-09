package org.kaschka.fersagers.discord.bot.listener.handler;

import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.utils.Logger;

public class XDHandler implements ChatHandler {

    private static final Pattern XD_REGEX = Pattern.compile("(?!(xD))\\b(([xX])+([dD])+)+", Pattern.MULTILINE);
    private final static Logger logger = Logger.getInstance();

    @Override
    public boolean handle(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if (containsXD(message.getContentRaw())) {
            fixXDCase(message);
            return true;
        }
        return false;
    }

    private void fixXDCase(Message message) {
        MessageChannel channel = message.getChannel();

        String newMessage = String.format("```\n%s\n``` \nFTFY %s",
                message.getContentRaw().replaceAll(XD_REGEX.toString(), "xD"),
                message.getAuthor());

        channel.deleteMessageById(message.getId()).queue();
        channel.sendMessage(newMessage).queue();

        logger.log(String.format("%s@%s@%s: Replaced XD",
                message.getAuthor().getName(),
                message.getGuild().getName(),
                message.getChannel().getName()));
    }

    private boolean containsXD(String string) {
        return XD_REGEX.matcher(string).find();
    }
}
