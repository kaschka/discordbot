package org.kaschka.fersagers.discord.bot.listener;


import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.kaschka.fersagers.discord.bot.utils.Logger;

public class JDAReadyListener extends ListenerAdapter {

    private static final String LOG_CHANNEL_ID = "634532515952197679";

    @Override
    public void onReady(ReadyEvent event) {
            Logger.getInstance().setLogChannel(event.getJDA().getTextChannelById(LOG_CHANNEL_ID));
            Logger.getInstance().log("----------------Started!--------------------");
    }
}
