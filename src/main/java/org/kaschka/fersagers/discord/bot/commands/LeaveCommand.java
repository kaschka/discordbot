package org.kaschka.fersagers.discord.bot.commands;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.audio.PlayerManager;

import static org.kaschka.fersagers.discord.bot.utils.MessageUtils.logAndDeleteMessage;

public class LeaveCommand implements Command {
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        logAndDeleteMessage(event);

        event.getGuild().getAudioManager().closeAudioConnection();
        PlayerManager.getInstance().stop(event.getGuild());
    }

    @Override
    public String getInvoke() {
        return "leave";
    }
}
