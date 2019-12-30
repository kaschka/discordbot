package org.kaschka.fersagers.discord.bot.configuration.permission;

public enum Role {
    BOT_PERMISSIONS(631956468731609119L);

    private long id;

    Role(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
