package org.kaschka.fersagers.discord.controller;

import org.kaschka.fersagers.discord.database.dao.SoundBoardDAO;
import org.kaschka.fersagers.discord.database.model.Sound;
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
public class SoundBoardController {

    private final SoundBoardDAO soundBoardDAO;

    @Autowired
    public SoundBoardController(SoundBoardDAO soundBoardDAO) {
        this.soundBoardDAO = soundBoardDAO;
    }

    @GetMapping("/sound/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Sound getSound(@PathVariable String id) {
        return soundBoardDAO.getSound(id);
    }

    @DeleteMapping("/sound/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSound(@PathVariable String id) {
        soundBoardDAO.removeSound(id);
    }

    @PostMapping("/sound")
    @ResponseStatus(HttpStatus.CREATED)
    public void setSound(@RequestBody Sound sound) {
        soundBoardDAO.setSound(sound);
    }
}
