package org.kaschka.fersagers.discord.bot.commands;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kaschka.fersagers.discord.bot.configuration.permission.Permissions;
import org.kaschka.fersagers.discord.bot.configuration.permission.Role;
import org.kaschka.fersagers.discord.bot.db.DbService;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class NewGuildCommand implements Command {

    private final DbService dbService;

    public NewGuildCommand() {
        this.dbService = new DbService();
    }

    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        assertGuildCommand(args, event);

        long musicChannel = Long.parseLong(args.get(0));
        dbService.addNewGuild(event.getGuild().getIdLong(), musicChannel, 0);
        MessageUtils.sendMessageToUser(event.getAuthor(), "Created");
    }

    private void assertGuildCommand(List<String> args, MessageReceivedEvent event) {
        if (args.size() != 1) {
            MessageUtils.sendMessageToUser(event.getAuthor(), "Invalid args.\n Use /guild [musicChannel Id]");
            throw new RuntimeException();
        }
    }

    @Override
    public String getInvoke() {
        return "guild";
    }

    @Override
    public String getHelp() {
        return "configures the guild";
    }

    @Override
    public Permissions requiredPermissions() {
        Permissions permissions = new Permissions();
        permissions.addRole(Role.NO_ONE_BUT_ID);
        return permissions;
    }
}
