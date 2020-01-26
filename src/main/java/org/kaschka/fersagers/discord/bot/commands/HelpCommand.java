package org.kaschka.fersagers.discord.bot.commands;

import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;


public class HelpCommand implements Command {

    private List<String> commands;

    public HelpCommand(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        MessageUtils.sendMessageToUser(event.getAuthor(), "Commands: " + commands.stream()
                .map(e -> "\n-> " + e)
                .collect(Collectors.joining()
                )
        );
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
        return "/help: Shows all Commands";
    }
}
