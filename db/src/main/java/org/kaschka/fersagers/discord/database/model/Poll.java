package org.kaschka.fersagers.discord.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


@Entity
@Table(name = "poll")
public class Poll {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "start", nullable = false)
    @JsonProperty("start")
    private long start;

    @Column(name = "endTime", nullable = false)
    @JsonProperty("endTime")
    private long endTime;

    @Column(name = "messageId", nullable = false)
    private long messageId;

    @Column(name = "channelId", nullable = false)
    private long channelId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }
}
