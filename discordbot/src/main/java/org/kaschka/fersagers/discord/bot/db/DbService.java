package org.kaschka.fersagers.discord.bot.db;

import java.io.IOException;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DbService {

    private final DbServiceRetro dbService;
    private final PassiveExpiringMap<Long, Long> cache;

    public DbService() {
        Retrofit build = new Retrofit.Builder().baseUrl("http://localhost:8081").addConverterFactory(JacksonConverterFactory.create()).build();
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

    public long getMusicChannel(long guildId) {
        if(cache.containsKey(guildId)) {
            return guildId;
        } else {
            try {
                Long id = dbService.getMusicChannel(guildId).execute().body();
                cache.put(guildId, id);
                return id;
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

    }
}
