package org.kaschka.fersagers.discord.bot.db;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SoundTO {
    @JsonProperty("id")
    String id;

    @JsonProperty("url")
    String url;

    public SoundTO() {
    }

    public SoundTO(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
