package org.kaschka.fersagers.discord.bot.configuration.permission;

public enum Role {
    BOT_PERMISSIONS("Bot Permissions"),
    EVERYONE("EVERYONE"),
    NO_ONE_BUT_ID("NO_ONE_BUT_ID");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
