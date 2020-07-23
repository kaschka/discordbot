package org.kaschka.fersagers.discord.controller.to;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuildTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("musicChannel")
    private long musicChannel;

    @JsonProperty("role")
    private long role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
