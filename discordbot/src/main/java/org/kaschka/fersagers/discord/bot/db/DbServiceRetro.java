package org.kaschka.fersagers.discord.bot.db;

import java.util.List;

import org.kaschka.fersagers.discord.bot.commands.poll.Poll;
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

    @POST("/api/poll")
    Call<Void> addPoll(@Body Poll poll);

    @GET("/api/poll")
    Call<List<Poll>> getPolls();

    @DELETE("/api/poll/{messageId}/channel/{channelId}")
    Call<Void> deletePoll(@Path("channelId") long channelId, @Path("messageId") long messageId);


}
