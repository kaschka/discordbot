package org.kaschka.fersagers.discord.controller;

import java.util.List;

import org.kaschka.fersagers.discord.database.dao.PollDAO;
import org.kaschka.fersagers.discord.database.model.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PollController {

    private final PollDAO pollDao;

    @Autowired
    public PollController(PollDAO pollDao) {
        this.pollDao = pollDao;
    }

    @PostMapping("/poll")
    @ResponseStatus(HttpStatus.OK)
    public void addPoll(@RequestBody Poll poll) {
        pollDao.addPoll(poll);
    }

    @GetMapping("/poll")
    @ResponseStatus(HttpStatus.OK)
    public List<Poll> getPolls() {
        return pollDao.getPolls();
    }

    @DeleteMapping("/poll/{messageId}/channel/{channelId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePoll(@PathVariable long channelId, @PathVariable long messageId) {
        pollDao.deletePoll(channelId, messageId);
    }
}
