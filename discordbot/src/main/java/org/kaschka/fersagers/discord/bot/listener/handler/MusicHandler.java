package org.kaschka.fersagers.discord.bot.listener.handler;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.commons.validator.routines.UrlValidator;
import org.kaschka.fersagers.discord.bot.db.DbService;
import org.kaschka.fersagers.discord.bot.utils.Logger;
import org.kaschka.fersagers.discord.bot.utils.MessageUtils;

public class MusicHandler implements ChatHandler, EventListener {

    private final static Logger logger = Logger.getInstance();

    private final DbService dbService;

    public MusicHandler() {
        this.dbService = new DbService();
    }

    @Override
    public boolean handle(MessageReceivedEvent event) {
        if(event.getChannel().getIdLong() == dbService.getMusicChannel(event.getGuild().getIdLong())) {
            if(isMusic(event.getMessage().getContentRaw())) {
                event.getMessage().delete().queue();

                MessageUtils.sendMessageToUser(event.getAuthor(), String.format("Only URLs are allowed in the %s channel!", event.getChannel().getName()));
                logger.log(String.format("User %s@%s@%s wrote in %s Channel: %s",
                        event.getAuthor().getName(),
                        event.getGuild().getName(),
                        event.getChannel().getName(),
                        event.getChannel().getName(),
                        event.getMessage().getContentRaw()));
            }
            return true;
        }
        return false;
    }

    private boolean isMusic(String text) {
        return !UrlValidator.getInstance().isValid(text) && isMusicExceptionCase(text);
    }

    private boolean isMusicExceptionCase(String text) {
        String musicExceptionCase = text.split("\\s+")[0];
        return !musicExceptionCase.startsWith("!musik");
    }

    @Override
    public void onEvent(GenericEvent event) {
        if(event instanceof ReadyEvent) {
            new Thread(() -> {
                for (Guild guild : event.getJDA().getGuilds()) {
                    long musicChannelId = getMusicChannelId(guild.getIdLong());
                    TextChannel musicChannel = guild.getTextChannelById(musicChannelId);
                    List<Message> retrievedMessages = musicChannel.getHistoryAround(musicChannel.getLatestMessageId(), 50).complete().getRetrievedHistory();

                    retrievedMessages.parallelStream()
                            .filter(m -> isMusic(m.getContentRaw()))
                            .forEach(m -> m.delete().queue());
                }
            }).start();
        }
    }

    private long getMusicChannelId(long id) {
        int tries = 0;

        do {
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

        } while(tries <= 5);

        RuntimeException e = new RuntimeException("Could not Fetch GuildId");
        logger.logException(e);
        throw e;
    }
}
