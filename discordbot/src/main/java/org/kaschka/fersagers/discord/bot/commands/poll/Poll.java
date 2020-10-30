package org.kaschka.fersagers.discord.bot.commands.poll;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Poll {
    private int id;
    @JsonProperty("start")
    private long startTime;
    @JsonProperty("endTime")
    private long endTime;
    private long messageId;
    private long channelId;

    public Poll() {}

    public Poll(long channelId, long startTime, long endTime) {
        this.channelId = channelId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
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

    public String getHumanReadableDuration() {
        Duration duration = Duration.of(endTime - Instant.now().toEpochMilli(), ChronoUnit.MILLIS);
        if(duration.toDays() > 0) {
            return duration.toDays() + "d";
        }

        if(duration.toHours() > 0) {
            return duration.toHours() + "h";
        }

        if(duration.toMinutes() > 0) {
            return duration.toMinutes() + "m";
        }

        if(duration.toMillis() > 1000) {
            return duration.toMillis() * 1000 + "s";
        }

        return "Less then 1s";
    }
}
