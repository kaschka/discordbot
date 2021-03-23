package org.kaschka.fersagers.discord.bot.commands.audio.soundboard;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.commands.audio.PlayCommand;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.db.DbService;
import org.kaschka.fersagers.discord.bot.db.SoundTO;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import java.util.List;

public class SoundBoardCommand implements Command {

    private final DbService dbService;

    public SoundBoardCommand() {
        this.dbService = new DbService();
    }

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        if (args.size() != 1) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Invalid args.\nUse /sound [ID]");
            return;
        }

        SoundTO sound = dbService.getSound(event.getGuild().getIdLong(), args.get(0));
        if (sound == null) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Sound not found!");
        } else {
            String url = sound.getUrl();
            PlayCommand.playSound(event, url, -1);
        }
    }

    @Override
    public String getInvoke() {
        return "sound";
    }

    @Override
    public String getHelp() {
        return "/sound [ID]: Plays the given sound";
    }

    @Override
    public Permissions requiredPermissions() {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.BOT_PERMISSIONS);
        return permissions;
    }
}
