package org.kaschka.fersagers.discord.bot.commands.audio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import static org.kaschka.fersagers.discord.bot.utils.MessageUtils.logAndDeleteMessage;

public class ClearCommand implements Command {

    List<Long> allowedIds = new ArrayList(Arrays.asList(407250792756674561L, 138025874384486400L));

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        logAndDeleteMessage(event);

        if(allowedIds.contains(event.getAuthor().getIdLong())) {
            PlayerManager.getInstance().clear();
            MessageUtils.sendMessageToUser(event.getAuthor(), "Cleared Track Queue!");
        } else {
            MessageUtils.sendMessageToUser(event.getAuthor(), "No Permissions!");
        }
    }

    @Override
    public boolean isDirectMessageEnabled() {
        return true;
    }

    @Override
    public String getInvoke() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return "/clear: Clears the Track Queues of all Servers";
    }
}
