package org.kaschka.fersagers.discord.controller.to;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuildTO {

    @JsonProperty("guildId")
    private long guildId;

    @JsonProperty("musicChannel")
    private long musicChannel;

    @JsonProperty("role")
    private long role;

    public long getId() {
        return guildId;
    }

    public void setId(long id) {
        this.guildId = id;
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
