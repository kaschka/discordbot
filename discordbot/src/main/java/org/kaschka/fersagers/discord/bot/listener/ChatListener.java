package org.kaschka.fersagers.discord.bot.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.RandomStringUtils;
import org.kaschka.fersagers.discord.bot.commands.CommandHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.ChatHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.HelloHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.MusicHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.XDHandler;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class ChatListener extends ListenerAdapter {

    private final CommandHandler commandHandler;

    private final static String PREFIX = "/";
    private static final String PATTERN_QUOTE = Pattern.quote(PREFIX);

    private final static Logger logger = Logger.getInstance();

    private final static List<ChatHandler> chatHandlers = new ArrayList<>();

    public ChatListener() {
        commandHandler = new CommandHandler();
        chatHandlers.add(new MusicHandler());
        chatHandlers.add(new XDHandler());
        chatHandlers.add(new HelloHandler());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        logger.setLogSessionId(RandomStringUtils.randomAlphanumeric(8));

        if(!event.getAuthor().isBot()) {
            final String message = event.getMessage().getContentRaw();
            final String[] split = message.replaceFirst("(?i)" + PATTERN_QUOTE, "").split("\\s+");
            final String invoke = split[0].toLowerCase();
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            if(message.startsWith(PREFIX) && commandHandler.isCommand(invoke)) {
                MessageUtils.logAndDeleteMessage(event);
                commandHandler.handleCommand(event, invoke, args);
            } else {
                // calls all handlers until one returns true
                chatHandlers.stream().anyMatch(e -> e.handle(event));
            }
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<Guild> guilds = event.getJDA().getGuilds();
        chatHandlers.stream().parallel().forEach(m -> m.handleOnStartup(guilds));
    }
}
