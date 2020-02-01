package org.kaschka.fersagers.discord.bot.configuration;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.kaschka.fersagers.discord.bot.listener.ChatListener;

public class ApplicationConfiguration {

    private static String CLIENT_TOKEN;
    public static ShardManager SHARD_MANAGER;

    public static final String STATUS = "Beep, Boop, I am a bot!";

    public static void main(String[] args) {
        if(args.length > 1) {
            throw new IllegalArgumentException("To many args!");
        } else if (args.length == 0) {
            throw new IllegalArgumentException("Api Key Required!");
        }

        CLIENT_TOKEN = args[0];

        SHARD_MANAGER = getShardManager();
        addEventListeners(SHARD_MANAGER);
    }

    private static void addEventListeners(ShardManager shardManager) {
        shardManager.addEventListener(new ChatListener());
        shardManager.addEventListener(new JDAReadyListener());
    }

    private static ShardManager getShardManager() {
        try {
            return new DefaultShardManagerBuilder()
                    .setToken(CLIENT_TOKEN)
                    .setActivity(Activity.playing(STATUS))
                    .setAutoReconnect(true)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Could not create JDA. Please verify the token");
    }
}
