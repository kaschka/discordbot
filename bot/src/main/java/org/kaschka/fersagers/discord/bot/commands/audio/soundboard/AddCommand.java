package org.kaschka.fersagers.discord.bot.commands.audio.soundboard;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.db.DbService;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

import java.util.List;

public class AddCommand implements Command {

    private final DbService dbService;

    public AddCommand() {
        this.dbService = new DbService();
    }

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        if (args.size() != 2) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Invalid args.\nUse /sound-add [ID] [URL]");
            return;
        }

        if (dbService.addSound(event.getGuild().getIdLong(), args.get(0), args.get(1))) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Sound added!");
        } else {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Sound already exists!");
        }
    }

    @Override
    public String getInvoke() {
        return "sound-add";
    }

    @Override
    public String getHelp() {
        return "/sound-add [ID] [URL]: Adds the given sound";
    }

    @Override
    public Permissions requiredPermissions() {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.BOT_PERMISSIONS);
        return permissions;
    }
}
