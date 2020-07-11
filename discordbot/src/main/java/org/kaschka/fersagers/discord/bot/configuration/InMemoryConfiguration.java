package org.kaschka.fersagers.discord.bot.configuration;

import java.util.HashMap;

public class InMemoryConfiguration {
    public static HashMap<Long, Boolean> isManuallyJoined = new HashMap<>(0);

    public static boolean isMannualyJoined(Long guild) {
        return isManuallyJoined.containsKey(guild) && isManuallyJoined.get(guild);
    }
}
