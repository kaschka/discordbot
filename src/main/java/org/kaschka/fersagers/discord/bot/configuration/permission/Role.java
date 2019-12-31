package org.kaschka.fersagers.discord.bot.configuration.permission;

public enum Role {
    BOT_PERMISSIONS("Bot Permissions"),
    NO_ROLE("no-role");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
