package org.kaschka.fersagers.discord.bot.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.commands.audio.HelpCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.LeaveCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.PlayCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.ShowCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.SkipCommand;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class CommandHandler {

    private final static String PREFIX = "/";

    private final static Logger logger = Logger.getInstance();

    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler() {
        addCommand(new PlayCommand());
        addCommand(new LeaveCommand());
        addCommand(new SkipCommand());
        addCommand(new ShowCommand());
        addCommand(new FuckCommand());
        addCommand(new HelpCommand(commands));
    }

    private void addCommand(Command command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }

    public boolean isDirectMessageEnabled(String commandName) {
        return commands.get(commandName).isDirectMessageEnabled();
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public boolean handleCommand(MessageReceivedEvent event) {
        final String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(PREFIX), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        boolean invoked = false;
        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            if(event.isFromType(ChannelType.PRIVATE) && !isDirectMessageEnabled(invoke)) {
                answerOnDirectMessage(event);
            } else {
                commands.get(invoke).handle(args, event);
                invoked = true;
            }
        }
        return invoked;
    }

    private void answerOnDirectMessage(MessageReceivedEvent event) {
            logger.logChatMessage(event);
            MessageUtils.sendMessageToUser(event.getAuthor(), "Beep, Boop, I am a bot!");
    }
}
