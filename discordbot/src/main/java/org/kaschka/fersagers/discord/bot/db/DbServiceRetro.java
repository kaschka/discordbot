package org.kaschka.fersagers.discord.bot.db;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DbServiceRetro {
    @GET("/api/guild/{guildId}/music")
    Call<Long> getMusicChannel(@Path("guildId") long guildId);

    @POST("/api/guild")
    Call<Void> createNewGuild(@Body GuildTO guildTO);
}
