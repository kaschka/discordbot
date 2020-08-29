package org.kaschka.fersagers.discord.bot.audio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import org.kaschka.fersagers.discord.bot.configuration.InMemoryConfiguration;
import org.kaschka.fersagers.discord.bot.utils.Logger;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;

    private final BlockingQueue<AudioTrack> queue;

    private final Guild guild;

    public TrackScheduler(AudioPlayer player, Guild guild) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.guild = guild;
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), true);
    }

    public void clearQueue() {
        queue.clear();
        player.stopTrack();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        boolean isManuallyJoined = InMemoryConfiguration.isMannualyJoined(guild.getIdLong());
        if(queue.isEmpty() && !isManuallyJoined) {
            guild.getAudioManager().closeAudioConnection();
        }
        nextTrack();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        Logger.getInstance().log("stuck");
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }
}
