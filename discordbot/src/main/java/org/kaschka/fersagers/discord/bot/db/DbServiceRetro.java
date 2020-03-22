package org.kaschka.fersagers.discord.bot.db;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DbServiceRetro {
    @GET("/api/guild/{guildId}/music")
    Call<Long> getMusicChannel(@Path("guildId") long guildId);

    @POST("/api/guild")
    Call<Void> createNewGuild(@Body GuildTO guildTO);

    @POST("/api/sound")
    Call<Void> createNewSound(@Body SoundTO soundTO);

    @GET("/api/sound/{id}")
    Call<SoundTO> getSound(@Path("id") String id);

    @GET("/api/sound")
    Call<List<SoundTO>> getSounds();

    @DELETE("/api/sound/{id}")
    Call<Void> deleteSound(@Path("id") String id);
}
