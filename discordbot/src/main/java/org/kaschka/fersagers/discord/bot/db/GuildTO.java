package org.kaschka.fersagers.discord.bot.db;

public class GuildTO {

    public GuildTO(long guildId, long musicChannel, long role) {
        this.guildId= guildId;
        this.musicChannel = musicChannel;
        this.role = role;
    }

    long guildId;
    long musicChannel;
    long role;

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getMusicChannel() {
        return musicChannel;
    }

    public void setMusicChannel(long musicChannel) {
        this.musicChannel = musicChannel;
    }

    public long getRole() {
        return role;
    }

    public void setRole(long role) {
        this.role = role;
    }
}
