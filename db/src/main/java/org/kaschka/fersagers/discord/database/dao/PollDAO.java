package org.kaschka.fersagers.discord.database.dao;

import org.kaschka.fersagers.discord.database.model.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PollDAO {

    private final PollRepository pollRepository;

    @Autowired
    public PollDAO(PollRepository pollRepository, RoleDAO roleDAO) {
        this.pollRepository = pollRepository;
    }

    public void addPoll(Poll poll) {
        pollRepository.save(poll);
    }

    public List<Poll> getPolls() {
        return pollRepository.getAllBy();
    }

    public void deletePoll(long channelId, long messageId) {
        pollRepository.deleteByChannelIdAndMessageId(channelId, messageId);
    }

}
