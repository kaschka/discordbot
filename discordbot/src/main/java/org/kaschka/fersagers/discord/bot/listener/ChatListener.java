package org.kaschka.fersagers.discord.bot.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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

public class ChatListener extends ListenerAdapter {

    private final CommandHandler commandHandler;

    private final static String PREFIX = "/";
    private static final String PATTERN_QUOTE = Pattern.quote(PREFIX);

    private final static Logger logger = Logger.getInstance();

    private final static List<ChatHandler> chatHandlers = new ArrayList<>();

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 255, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());

    public ChatListener() {
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
                    final List<String> args = parseArgs(split);
                    commandHandler.handleCommand(event, invoke, args);
                } else {
                    // calls all handlers until one returns true
                    chatHandlers.stream().anyMatch(e -> e.handle(event));

                }
            });
        }
    }

    private List<String> parseArgs(String[] unfilteredArgs) {
        final List<String> extractedArgs = new ArrayList<>();
        int firstBracketPosition = 0, lastBracketPosition = 0;

        for (int i = 1; i < unfilteredArgs.length; i++) {
            if (isOpeningBracket(unfilteredArgs[i])) {
                firstBracketPosition = i;
            }

            if (isClosingBracket(unfilteredArgs[i])) {
                lastBracketPosition = i;
            }

            if (bothBracketsFound(firstBracketPosition, lastBracketPosition)) {
                firstBracketPosition = 0;
                lastBracketPosition = 0;
                extractedArgs.add(getArgBetweenBrackets(unfilteredArgs, firstBracketPosition, lastBracketPosition));

                //if no brackets are found yet, we must have a single element
            } else if (noBracketsFound(firstBracketPosition, lastBracketPosition)) {
                extractedArgs.add(unfilteredArgs[i]);
            }
        }

        removeBrackets(extractedArgs);
        return extractedArgs;
    }

    private boolean bothBracketsFound(int firstBracketPosition, int lastBracketPosition) {
        return firstBracketPosition != 0 && lastBracketPosition != 0;
    }

    private boolean noBracketsFound(int firstBracketPosition, int lastBracketPosition) {
        return firstBracketPosition == 0 && lastBracketPosition == 0;
    }

    private boolean isClosingBracket(String string) {
        return string.startsWith("}") || string.endsWith("}");
    }

    private boolean isOpeningBracket(String string) {
        return string.startsWith("{");
    }

    private String getArgBetweenBrackets(String[] unfilteredArgs, int firstBracketPosition, int lastBracketPosition) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = firstBracketPosition; i <= lastBracketPosition; i++) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(unfilteredArgs[i]);
        }
        return stringBuilder.toString();
    }

    private void removeBrackets(List<String> args) {
        for (int i = 0; i < args.size(); i++) {
            args.set(i, args.get(i).replaceAll("[{}]", ""));
        }
    }
}
