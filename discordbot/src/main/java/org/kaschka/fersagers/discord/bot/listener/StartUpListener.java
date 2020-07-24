package org.kaschka.fersagers.discord.bot.listener;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.kaschka.fersagers.discord.bot.listener.handler.MusicHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.PollStartUpHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.StartUpHandler;
import org.kaschka.fersagers.discord.bot.listener.handler.StartUpMessageHandler;

public class StartUpListener extends ListenerAdapter {

    private final static List<StartUpHandler> startUpHandlers = new ArrayList<>();

    public StartUpListener() {
        startUpHandlers.add(new StartUpMessageHandler());
        startUpHandlers.add(new MusicHandler());
        startUpHandlers.add(new PollStartUpHandler());
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<Guild> guilds = event.getJDA().getGuilds();
        startUpHandlers.stream().parallel().forEach(m -> m.handleOnStartup(guilds));
        startUpHandlers.stream().parallel().forEach(StartUpHandler::handleOnStartup);
    }
}
