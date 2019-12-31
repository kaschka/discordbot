package org.kaschka.fersagers.discord.bot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.MessageUtils.logAndDeleteMessage;

public class HelpCommand implements Command {

    private List<String> commands;

    public HelpCommand(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        logAndDeleteMessage(event);

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
