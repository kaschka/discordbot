package org.kaschka.fersagers.discord.bot.listener;

import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.kaschka.fersagers.discord.bot.handler.CommandHandler;
import org.kaschka.fersagers.discord.bot.handler.CommandHandlerImpl;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class ChatListener extends ListenerAdapter {

    private static final Pattern XD_REGEX = Pattern.compile("(?!(xD(?!\\w)))\\b((x|X)+(d|D)+)+", Pattern.MULTILINE);

    private static final String MUSIC_CHANNEL = "musik";

    private final static Logger logger = Logger.getInstance();

    private CommandHandler commandHandler;

    public ChatListener() {
        commandHandler = new CommandHandlerImpl();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        logger.setLogSessionId(RandomStringUtils.randomAlphanumeric(8));
        if(event.isFromType(ChannelType.TEXT) && !event.getAuthor().isBot()) {
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
        MessageChannel channel = event.getChannel();

        String newMessage = String.format("```\n%s\n``` \nFTFY %s",
                event.getMessage().getContentRaw()
                        .replaceAll(XD_REGEX.toString(), "xD"),
                event.getAuthor());

        channel.deleteMessageById(event.getMessageId()).queue();
        channel.sendMessage(newMessage).queue();

        logger.log(String.format("%s@%s@%s: Replaced XD",
                event.getAuthor().getName(),
                event.getGuild().getName(),
                event.getChannel().getName()));
    }

    private void handleMusicChannelMessage(MessageReceivedEvent event) {
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
    }

    private boolean containsXD(String string) {
        return XD_REGEX.matcher(string).find();
    }
}
