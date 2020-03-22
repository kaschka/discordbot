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
}
