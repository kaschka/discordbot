package org.kaschka.fersagers.discord.database.dao;

import org.kaschka.fersagers.discord.database.model.Poll;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface PollRepository extends CrudRepository<Poll, Integer> {
    List<Poll> getAllBy();

    @Transactional
    void deleteByChannelIdAndMessageId(long channelId, long messageId);
}
