package org.kaschka.fersagers.discord.bot.listener;

import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.validator.routines.UrlValidator;
import org.kaschka.fersagers.discord.bot.handler.CommandHandler;
import org.kaschka.fersagers.discord.bot.handler.CommandHandlerImpl;
import org.kaschka.fersagers.discord.bot.utils.ChatLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatListener extends ListenerAdapter {

    private static final Pattern XD_REGEX = Pattern.compile("(?!(xD(?!\\w)))\\b((x|X)+(d|D)+)+", Pattern.MULTILINE);

    private static final String MUSIC_CHANNEL = "musik";

    private Logger logger = LoggerFactory.getLogger("ChatListener");
    private ChatLogger chatLogger = new ChatLogger();

    private CommandHandler commandHandler;

    public ChatListener() {
        commandHandler = new CommandHandlerImpl();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.isFromType(ChannelType.TEXT) && !event.getAuthor().isBot()) {
            chatLogger.logChatMessage(event);
            if(!isCommand(event)) {
                if(event.getChannel().getName().equals(MUSIC_CHANNEL)) {
                    handleMusicChannelMessage(event);
                } else if(containsXD(event.getMessage().getContentRaw())) {
                    editMessage(event);
                }
            }
        }
    }

    private boolean isCommand(MessageReceivedEvent event) {
        return commandHandler.handle(event.getMessage().getContentRaw(), event);
    }

    private void editMessage(MessageReceivedEvent event) {
        String messageID = event.getMessageId();
        MessageChannel channel = event.getChannel();

        String newMessage = String.format("```\n%s\n``` \nFTFY %s",
                event.getMessage().getContentRaw()
                        .replaceAll(XD_REGEX.toString(), "xD"),
                event.getAuthor());
        channel.deleteMessageById(messageID).complete();
        channel.sendMessage(newMessage).complete();
        logger.debug("Replaced XD from: " + event.getAuthor().getName() + "@" + event.getGuild().getName() + "@" + event.getChannel().getName());
    }

    private void handleMusicChannelMessage(MessageReceivedEvent event) {
        if(!UrlValidator.getInstance().isValid(event.getMessage().getContentRaw())) {
            event.getMessage().delete().queue();
            event.getAuthor().openPrivateChannel().queue((channel) -> channel.sendMessage(String.format("Only URLs are allowed in the %s channel!", event.getChannel().getName())).queue());
        }
    }

    private boolean containsXD(String string) {
        return XD_REGEX.matcher(string).find();
    }
}
