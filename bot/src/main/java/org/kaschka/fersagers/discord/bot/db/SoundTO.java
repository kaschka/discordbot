package org.kaschka.fersagers.discord.bot.db;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SoundTO {

    @JsonProperty("id")
    long id;

    @JsonProperty("guildId")
    long guildId;

    @JsonProperty("name")
    String name;

    @JsonProperty("url")
    String url;

    public SoundTO() {
    }

    public SoundTO(long guildId, String name, String url) {
        this.guildId = guildId;
        this.name = name;
        this.url = url;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
