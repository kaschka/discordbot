package org.kaschka.fersagers.discord.bot.listener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.kaschka.fersagers.discord.bot.commands.CommandHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.ChatHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.HelloHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.MusicHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.XDHandler;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ChatListener extends ListenerAdapter {

    private final CommandHandler commandHandler;

    private final static String PREFIX = "/";
    private static final String PATTERN_QUOTE = Pattern.quote(PREFIX);

    private final ArgParser argParser;

    private final static Logger logger = Logger.getInstance();

    private final static List<ChatHandler> chatHandlers = new ArrayList<>();

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 255, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());

    public ChatListener(ArgParser argParser) {
        this.argParser = argParser;

        commandHandler = new CommandHandler();
        chatHandlers.add(new MusicHandler());
        chatHandlers.add(new XDHandler());
        chatHandlers.add(new HelloHandler());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            executor.submit(() -> {
                logger.setLogSessionId(RandomStringUtils.randomAlphanumeric(8));
                final String message = event.getMessage().getContentRaw();
                final String[] split = message.replaceFirst("(?i)" + PATTERN_QUOTE, "").split("\\s+");
                final String invoke = split[0].toLowerCase();

                if (message.startsWith(PREFIX) && commandHandler.isCommand(invoke)) {
                    MessageUtils.logAndDeleteMessage(event);
                    final String[] unfilteredArgsWithoutInvoke = Arrays.copyOfRange(split, 1, split.length);
                    final List<String> args = argParser.parse(unfilteredArgsWithoutInvoke);
                    commandHandler.handleCommand(event, invoke, args);
                } else {
                    // calls all handlers until one returns true
                    chatHandlers.stream().anyMatch(e -> e.handle(event));

                }
            });
        }
    }
}
