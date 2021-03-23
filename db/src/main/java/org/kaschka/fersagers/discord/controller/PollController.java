package org.kaschka.fersagers.discord.controller;

import org.kaschka.fersagers.discord.database.dao.PollDAO;
import org.kaschka.fersagers.discord.database.model.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
