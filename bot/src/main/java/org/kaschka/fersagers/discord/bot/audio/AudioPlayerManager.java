package org.kaschka.fersagers.discord.bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.kaschka.fersagers.discord.bot.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AudioPlayerManager {
    private static final Logger logger = Logger.getInstance();
    private static AudioPlayerManager INSTANCE;
    private final com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager audioPlayerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    private final static int MAX_QUEUE_SIZE = 100;

    private final static Pattern youtubeRegex = Pattern.compile(
            "^.*?(youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=|\\&v=)([^#\\&\\?]*)(?:(\\?t|&start)=(\\d+))?.*",
            Pattern.CASE_INSENSITIVE);
    private final static int START_TIME_REGEX_GROUP = 4;

    private AudioPlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public synchronized void clear() {
        musicManagers.values().parallelStream().forEach(e -> e.scheduler.getQueue().clear());
        musicManagers.values().parallelStream().forEach(e -> e.player.stopTrack());
        musicManagers.values().parallelStream().forEach(e -> e.guild.getAudioManager().closeAudioConnection());
        musicManagers.clear();
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(audioPlayerManager, guild);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void stop(Guild guild) {
        if (musicManagers.containsKey(guild.getIdLong())) {
            musicManagers.get(guild.getIdLong()).scheduler.clearQueue();
        }
        guild.getAudioManager().closeAudioConnection();
    }

    public void loadAndPlay(VoiceChannel channel, String trackUrl) throws RuntimeException {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        if (musicManager.scheduler.getQueue().size() >= MAX_QUEUE_SIZE) {
            logger.log("Queue is full!");
            throw new RuntimeException("Full queue");
        }

        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setPosition(getStartTimeMillisFromUrl(trackUrl));
                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    play(musicManager, track);
                }
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                logger.logException(exception);
                stop(channel.getGuild());
            }
        });
    }

    private int getStartTimeMillisFromUrl(String trackUrl) {
        Matcher matcher = youtubeRegex.matcher(trackUrl);

        if (!matcher.matches()) {
            return 0;
        }

        try {
            return Integer.parseInt(matcher.group(START_TIME_REGEX_GROUP)) * 1000;
        } catch (Exception e) {
            return 0;
        }
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized AudioPlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AudioPlayerManager();
        }
        return INSTANCE;
    }
}
