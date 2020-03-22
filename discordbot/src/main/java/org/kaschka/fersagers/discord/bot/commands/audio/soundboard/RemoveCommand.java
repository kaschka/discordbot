package org.kaschka.fersagers.discord.bot.commands.audio.soundboard;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.commands.Command;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.RequiresPermission;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.db.DbService;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class RemoveCommand implements Command {

    private final DbService dbService;

    public RemoveCommand() {
        this.dbService = new DbService();
    }

    @Override
    @RequiresPermission
    public void handle(List<String> args, MessageReceivedEvent event) {
        if(args.size() != 1) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Invalid args.\nUse /sound-remove [id]");
            return;
        }

        dbService.deleteSound(args.get(0));
        MessageUtils.sendMessageToUser(event.getAuthor(), "Sound deleted!");
    }

    @Override
    public boolean isDirectMessageEnabled() {
        return true;
    }

    @Override
    public String getInvoke() {
        return "sound-delete";
    }

    @Override
    public String getHelp() {
        return "/sound-delete [id]: deletes the given sound";
    }

    @Override
    public Permissions requiredPermissions() {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.BOT_PERMISSIONS);
        return permissions;
    }
}
