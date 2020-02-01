package org.kaschka.fersagers.discord.bot.commands;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.configuration.permission.Permissions.hasPermission;


public class HelpCommand implements Command {

    private List<String> orderedCommandStrings;
    private Map<String, Command> commands;

    public HelpCommand(List<String> orderedCommandStrings, Map<String, Command> commands) {
        this.orderedCommandStrings = orderedCommandStrings;
        this.commands = commands;
    }

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        Stream<String> filteredStream = orderedCommandStrings.stream();

        if(!(args.size() > 0 && args.get(0).equals("all"))) {
            filteredStream = orderedCommandStrings.stream()
                    .filter(e -> hasPermission(commands.get(e).requiredPermissions(), event.getMember()) ||
                            commands.get(e).requiredPermissions().hasPermission(Role.EVERYONE) ||
                            commands.get(e).requiredPermissions().hasPermission(event.getAuthor().getIdLong()));
        }

        String helpString = filteredStream
                .map(e -> commands.get(e).getHelp())
                .map(e -> "\n-> " + e)
                .collect(Collectors.joining());

        MessageUtils.sendMessageToUser(event.getAuthor(), "Commands: " + helpString);
    }

    @Override
    public boolean isDirectMessageEnabled() {
        return true;
    }

    @Override
    public String getInvoke() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "/help [all]: Shows all available Commands";
    }
}
