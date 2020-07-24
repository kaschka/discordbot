package org.kaschka.fersagers.discord.bot.listener.handler;

import org.kaschka.fersagers.discord.bot.configuration.ApplicationConfiguration;
import org.kaschka.fersagers.discord.bot.utils.Logger;

public class StartUpMessageHandler implements StartUpHandler {
    private static final String LOG_CHANNEL_ID = "634532515952197679";

    @Override
    public void handleOnStartup() {
        Logger.getInstance().setLogChannel(ApplicationConfiguration.SHARD_MANAGER.getTextChannelById(LOG_CHANNEL_ID));
        Logger.getInstance().log("----------------Started!--------------------");
    }
}
