package org.kaschka.fersagers.discord.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Sound {

    @Id
    @Column(name = "id", nullable = false)
    @JsonProperty("id")
    private String id;

    @Column(name = "url", nullable = false)
    @JsonProperty("url")
    private String url;

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