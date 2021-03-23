package org.kaschka.fersagers.discord.bot.listener.handler;

import org.kaschka.fersagers.discord.bot.commands.poll.Poll;
import org.kaschka.fersagers.discord.bot.commands.poll.PollCommand;
import org.kaschka.fersagers.discord.bot.db.DbService;

import java.util.List;

public class PollStartUpHandler implements StartUpHandler {

    private static final DbService db = new DbService();

    @Override
    public void handleOnStartup() {
        try {
            //Wait for db connection
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Poll> polls = db.getAllPolls();
        polls.forEach(PollCommand.Refresher::refreshPoll);
    }
}
