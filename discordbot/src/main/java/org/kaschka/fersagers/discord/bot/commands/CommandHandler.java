package org.kaschka.fersagers.discord.bot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.commands.audio.ClearCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.LeaveCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.PlayCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.ShowCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.SkipCommand;
import org.kaschka.fersagers.discord.bot.configuration.ApplicationConfiguration;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class CommandHandler {

    private final static Logger logger = Logger.getInstance();

    private final Map<String, Command> commands = new HashMap<>();
    private final List<String> orderedInvoke = new ArrayList<>();

    public CommandHandler() {
        addCommand(new HelpCommand(orderedInvoke, commands));
        addCommand(new PlayCommand());
        addCommand(new LeaveCommand());
        addCommand(new ShowCommand());
        addCommand(new SkipCommand());
        addCommand(new ClearCommand());
        addCommand(new FuckCommand());
    }

    private void addCommand(Command command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
        orderedInvoke.add(command.getInvoke());
    }

    public boolean isCommand(String key) {
        return commands.containsKey(key);
    }

    public boolean isDirectMessageEnabled(String commandName) {
        return commands.get(commandName).isDirectMessageEnabled();
    }

    public void handleCommand(MessageReceivedEvent event, String invoke, List<String> args) {
        if(event.isFromType(ChannelType.PRIVATE) && !isDirectMessageEnabled(invoke)) {
            answerOnDirectMessage(event);
        } else {
            commands.get(invoke).handle(args, event);
        }
    }

    private void answerOnDirectMessage(MessageReceivedEvent event) {
            logger.logChatMessage(event);
            MessageUtils.sendMessageToUser(event.getAuthor(), ApplicationConfiguration.STATUS);
    }
}
