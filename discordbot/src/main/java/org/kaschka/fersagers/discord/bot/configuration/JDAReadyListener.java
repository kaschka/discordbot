package org.kaschka.fersagers.discord.bot.configuration;


import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.kaschka.fersagers.discord.bot.utils.Logger;

public class JDAReadyListener implements EventListener {

    private static final String LOG_CHANNEL_ID = "634532515952197679";

    @Override
    public void onEvent(GenericEvent event) {
        if(event instanceof ReadyEvent) {
            Logger.getInstance().setLogChannel(event.getJDA().getTextChannelById(LOG_CHANNEL_ID));
            Logger.getInstance().log("----------------Started!--------------------");
        }
    }
}
