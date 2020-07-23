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

    @POST("/api/guild/sound")
    Call<Void> createNewSound(@Body SoundTO soundTO);

    @GET("/api/guild/{guildId}/sound/{name}")
    Call<SoundTO> getSound(@Path("guildId") long guildId, @Path("name") String name);

    @GET("/api/guild/{guildId}/sound")
    Call<List<SoundTO>> getSounds(@Path("guildId") long guildId);

    @DELETE("/api/guild/{guildId}/sound/{name}")
    Call<Void> deleteSound(@Path("guildId") long guildId, @Path("name") String name);
}
