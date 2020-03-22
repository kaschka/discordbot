package org.kaschka.fersagers.discord.bot.db;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DbService {

    private final DbServiceRetro dbService;
    private final PassiveExpiringMap<Long, Long> cache;

    private final Logger logger = Logger.getInstance();

    public DbService() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        final Retrofit build = new Retrofit.Builder()
                        .baseUrl("http://localhost:8081")
                        .addConverterFactory(JacksonConverterFactory.create())
                        .client(okHttpClient)
                        .build();

        dbService = build.create(DbServiceRetro.class);
        cache = new PassiveExpiringMap<>(60*1000);
    }

    public void addNewGuild(long guildId, long musicChannel, long role) {
        try {
            dbService.createNewGuild(new GuildTO(guildId, musicChannel, role)).execute();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public SoundTO getSound(String id) {
        try {
            return dbService.getSound(id).execute().body();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public List<SoundTO> getSounds() {
        try {
            return dbService.getSounds().execute().body();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public boolean deleteSound(String id) {
        try {
            return dbService.deleteSound(id).execute().isSuccessful();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public boolean addSound(String id, String url) {
        try {
            return dbService.createNewSound(new SoundTO(id, url)).execute().isSuccessful();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public long getMusicChannel(long guildId) {
        if(cache.containsKey(guildId)) {
            return cache.get(guildId);
        } else {
            try {
                Long id = dbService.getMusicChannel(guildId).execute().body();
                cache.put(guildId, id);
                return id;
            } catch (IOException e) {
                logger.logException(e);
                throw new RuntimeException();
            }
        }

    }
}
