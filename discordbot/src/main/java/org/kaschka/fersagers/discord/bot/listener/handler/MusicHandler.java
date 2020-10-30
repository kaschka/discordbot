package org.kaschka.fersagers.discord.bot.listener.handler;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.validator.routines.UrlValidator;
import org.kaschka.fersagers.discord.bot.db.DbService;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class MusicHandler implements ChatHandler, StartUpHandler {

    private final static Logger logger = Logger.getInstance();

    private final DbService dbService;

    public MusicHandler() {
        this.dbService = new DbService();
    }

    @Override
    public boolean handle(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if(event.getChannel().getIdLong() == dbService.getMusicChannel(event.getGuild().getIdLong())) {
            if(isMusic(message.getContentRaw())) {
                MessageUtils.sendMessageToUser(message.getAuthor(), String.format("Only URLs are allowed in the %s channel!", message.getChannel().getName()));
                logAndDelete(message);
            }
            return true;
        }
        return false;
    }

    @Override
    public void handleOnStartup(List<Guild> guilds) {
        for (Guild guild : guilds) {
            long musicChannelId = getMusicChannelId(guild.getIdLong());
            TextChannel musicChannel = guild.getTextChannelById(musicChannelId);
            List<Message> retrievedMessages = musicChannel.getHistoryAround(musicChannel.getLatestMessageId(), 50).complete().getRetrievedHistory();

            retrievedMessages.parallelStream()
                    .filter(m -> isMusic(m.getContentRaw()))
                    .forEach(this::logAndDelete);
        }
    }

    private boolean isMusic(String text) {
        return !UrlValidator.getInstance().isValid(text) && isMusicExceptionCase(text);
    }

    private boolean isMusicExceptionCase(String text) {
        String musicExceptionCase = text.split("\\s+")[0];
        return !musicExceptionCase.startsWith("!musik");
    }

    private void logAndDelete(Message message) {
        logger.log(String.format("User %s@%s@%s wrote in %s Channel: %s",
                message.getAuthor().getName(),
                message.getGuild().getName(),
                message.getChannel().getName(),
                message.getChannel().getName(),
                message.getContentRaw()));

        message.delete().queue();
    }

    private long getMusicChannelId(long id) {
        int tries = 0;

        while(tries <= 5) {
            tries++;
            try {
                return dbService.getMusicChannel(id);
            } catch (Exception e) {
                try {
                    Thread.sleep((long) (1000*Math.pow(2, tries)));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        RuntimeException e = new RuntimeException("Could not Fetch GuildId");
        logger.logException(e);
        throw e;
    }
}
