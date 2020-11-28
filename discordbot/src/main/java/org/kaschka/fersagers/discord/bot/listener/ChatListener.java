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
                    final ArrayList<String> args = parseArgs(split);
                    commandHandler.handleCommand(event, invoke, args);
                } else {
                    // calls all handlers until one returns true
                    chatHandlers.stream().anyMatch(e -> e.handle(event));

                }
            });
        }
    }

    @NotNull
    private ArrayList<String> parseArgs(String[] split) {
        final ArrayList<String> args = new ArrayList<>();

        int start = 0, end = 0;
        for (int i = 1; i < split.length; i++) {
            if (split[i].startsWith("{")) {
                //remember the location of the first bracket
                start = i;
            }
            //remember the location of the second bracket, if its on start or end
            //If a element looks like: {someText} it's both, the start and the end position.
            if (split[i].startsWith("}") || split[i].endsWith("}")) {
                end = i;
            }

            //if both bracket locations are found, group all string elements between those two brackets
            if (end != 0 && start != 0) {
                StringBuilder s = new StringBuilder();
                for (int j = start; j <= end; j++) {
                    if (s.length() != 0) {
                        s.append(" ");
                    }
                    s.append(split[j]);
                }
                end = 0;
                start = 0;
                args.add(s.toString());
                //if no brackets are found yet, we must have a single element
            } else if (end == 0 && start == 0) {
                args.add(split[i]);
            }
        }

        //after the elements are grouped, remove the unnecessary brackets
        for (int i = 0; i < args.size(); i++) {
            args.set(i, args.get(i).replaceAll("[{}]", ""));
        }
        return args;
    }
}
