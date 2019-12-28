package org.kaschka.fersagers.discord.bot.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.commands.audio.LeaveCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.PlayCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.ShowCommand;
import org.kaschka.fersagers.discord.bot.commands.audio.SkipCommand;

public class CommandHandler {
    private final static String PREFIX = "/";

    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler() {
        addCommand(new FuckCommand());
        addCommand(new PlayCommand());
        addCommand(new LeaveCommand());
        addCommand(new ShowCommand());
        addCommand(new SkipCommand());
    }

    private void addCommand(Command command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }

    public Collection<Command> getCommands() {
        return commands.values();
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public void handleCommand(MessageReceivedEvent event) {
        final String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(PREFIX), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            commands.get(invoke).handle(args, event);
        }
    }
}
