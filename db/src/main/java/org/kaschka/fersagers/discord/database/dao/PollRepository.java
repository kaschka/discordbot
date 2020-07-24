package org.kaschka.fersagers.discord.database.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.kaschka.fersagers.discord.database.model.Poll;
import org.springframework.data.repository.CrudRepository;

public interface PollRepository extends CrudRepository<Poll, Integer> {
    List<Poll> getAllBy();

    @Transactional
    void deleteByChannelIdAndMessageId(long channelId, long messageId);
}
