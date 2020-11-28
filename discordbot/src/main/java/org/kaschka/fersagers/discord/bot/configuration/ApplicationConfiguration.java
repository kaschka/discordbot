package org.kaschka.fersagers.discord.bot.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.kaschka.fersagers.discord.bot.listener.ChatListener;
import org.kaschka.fersagers.discord.bot.listener.StartUpListener;

public class ApplicationConfiguration {

    private static String CLIENT_TOKEN;
    public static ShardManager SHARD_MANAGER;

    public static final String STATUS = "Beep, Boop, I am a bot!";

    public static void main(String[] args) {
        if (args.length > 1) {
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
        shardManager.addEventListener(new StartUpListener());
    }

    private static ShardManager getShardManager() {
        try {
            return DefaultShardManagerBuilder.create(getRequiredIntents())
                    .setToken(CLIENT_TOKEN)
                    .setActivity(Activity.playing(STATUS))
                    .setAutoReconnect(true)
                    .disableCache(getDisabledCacheFlags())
                    .setBulkDeleteSplittingEnabled(false)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Could not create JDA. Please verify the token");
    }

    private static List<GatewayIntent> getRequiredIntents() {
        List<GatewayIntent> list = new ArrayList();
        list.add(GatewayIntent.DIRECT_MESSAGES);
        list.add(GatewayIntent.GUILD_VOICE_STATES);
        list.add(GatewayIntent.GUILD_MESSAGES);
        list.add(GatewayIntent.GUILD_EMOJIS);
        return list;
    }

    private static List<CacheFlag> getDisabledCacheFlags() {
        List<CacheFlag> list = new ArrayList();
        list.add(CacheFlag.ACTIVITY);
        list.add(CacheFlag.CLIENT_STATUS);
        return list;
    }
}
