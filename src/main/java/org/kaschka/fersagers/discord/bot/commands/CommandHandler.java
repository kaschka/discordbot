package org.kaschka.fersagers.discord.bot.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHandler {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler() {
        addCommand(new FuckCommand());
        addCommand(new PlayCommand());
        addCommand(new LeaveCommand());
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
        final String prefix = "/";

        final String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            commands.get(invoke).handle(args, event);
        }
    }
}
