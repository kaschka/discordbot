package org.kaschka.fersagers.discord.bot.listener.handler;

import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.utils.Logger;

public class XDHandler implements ChatHandler {

    private static final Pattern XD_REGEX = Pattern.compile("(?!(xD(?!\\w)))\\b(([xX])+([dD])+)+", Pattern.MULTILINE);
    private final static Logger logger = Logger.getInstance();

    @Override
    public boolean handle(MessageReceivedEvent event) {
        if(containsXD(event.getMessage().getContentRaw())) {
            fixXDCase(event);
            return true;
        }
        return false;
    }

    private void fixXDCase(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();

        String newMessage = String.format("```\n%s\n``` \nFTFY %s",
                event.getMessage().getContentRaw().replaceAll(XD_REGEX.toString(), "xD"),
                event.getAuthor());

        channel.deleteMessageById(event.getMessageId()).queue();
        channel.sendMessage(newMessage).queue();

        logger.log(String.format("%s@%s@%s: Replaced XD",
                event.getAuthor().getName(),
                event.getGuild().getName(),
                event.getChannel().getName()));
    }

    private boolean containsXD(String string) {
        return XD_REGEX.matcher(string).find();
    }
}
