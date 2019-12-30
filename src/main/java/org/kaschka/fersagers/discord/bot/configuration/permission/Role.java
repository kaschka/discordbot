package org.kaschka.fersagers.discord.bot.configuration.permission;

public enum Role {
    BOT_PERMISSIONS("Bot Permissions"),
    NO_ROLE("");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
