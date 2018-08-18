package org.kaschka.fersagers.discord.bot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import org.kaschka.fersagers.discord.bot.listener.ChatListener;

public class ApplicationConfiguration {

    private static String CLIENT_TOKEN;

    public static void main(String[] args) {
        if(args.length > 1) {
            throw new IllegalArgumentException("To many args!");
        }
        CLIENT_TOKEN = args[0];

        ShardManager shardManager = getShardManager();
        shardManager.addEventListener(new ChatListener());
    }

    private static ShardManager getShardManager() {
        try {
            return new DefaultShardManagerBuilder().setToken(CLIENT_TOKEN).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Could not create JDA. Please verify the token");
    }
}
