package org.kaschka.fersagers.discord.bot.commands.audio.soundboard;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.db.DbService;
import org.kaschka.fersagers.discord.bot.db.SoundTO;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class ListCommand implements Command {

    private final DbService dbService;

    public ListCommand() {
        this.dbService = new DbService();
    }

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        List<SoundTO> sounds = dbService.getSounds();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The following sounds are available:\n");

        for (SoundTO sound : sounds) {
            stringBuilder
                    .append("    ->  ")
                    .append(sound.getId())
                    .append("\n");
        }

        MessageUtils.sendMessageToUser(event.getAuthor(), stringBuilder.toString());
    }

    @Override
    public boolean isDirectMessageEnabled() {
        return true;
    }

    @Override
    public String getInvoke() {
        return "sound-list";
    }

    @Override
    public String getHelp() {
        return "/sound-list: Lists all soundboard sounds";
    }
}
