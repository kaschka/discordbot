package org.kaschka.fersagers.discord.bot.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.kaschka.fersagers.discord.bot.listener.handler.MusicHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.PollStartUpHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.StartUpHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.StartUpMessageHandler;

public class StartUpListener extends ListenerAdapter {

    private static final List<StartUpHandler> startUpHandlers = new ArrayList<>();
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 10, 10L, TimeUnit.SECONDS, new SynchronousQueue<>());

    public StartUpListener() {
        startUpHandlers.add(new StartUpMessageHandler());
        startUpHandlers.add(new MusicHandler());
        startUpHandlers.add(new PollStartUpHandler());
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<Guild> guilds = event.getJDA().getGuilds();
        startUpHandlers.forEach(handler -> executor.submit(() -> handler.handleOnStartup(guilds)) );
        startUpHandlers.forEach(handler -> executor.submit((Runnable) handler::handleOnStartup));

        //clean up, as it only runs on startup
        startUpHandlers.clear();
    }
}
