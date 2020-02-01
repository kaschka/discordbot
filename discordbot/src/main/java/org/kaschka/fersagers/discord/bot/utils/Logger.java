package org.kaschka.fersagers.discord.bot.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Logger {

    private static Logger INSTANCE;
    private static TextChannel logChannel;

    private static ThreadLocal<String> logSessionId = new ThreadLocal<>();

    private Logger(){}

    public static synchronized Logger getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Logger();
        }
        return INSTANCE;
    }

    public void log(String string) {
        logFormatted(String.format("%s", string));
    }

    public void logChatMessage(MessageReceivedEvent message) {
        switch (message.getChannelType()) {
            case PRIVATE: logFormatted(
                    String.format(
                            "[Private] %s: %s",
                            message.getAuthor().getName(),
                            message.getMessage()
                    )); break;

            case TEXT: logFormatted(
                    String.format(
                            "[%s@%s] %s: %s",
                            message.getGuild().getName(),
                            message.getChannel().getName(),
                            message.getAuthor().getName(),
                            message.getMessage()
                    )); break;
        }
    }

    public void setLogChannel(TextChannel channel) {
        if(logChannel == null) {
            logChannel = channel;
        }
    }

    public void setLogSessionId(String id) {
        logSessionId.set(id);
    }

    private void logFormatted(String string) {
        logChannel.sendMessage(String.format("%s [%s] %s",
                new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()),
                logSessionId.get(),
                string
                )
        ).queue();
    }
}