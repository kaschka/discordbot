package org.kaschka.fersagers.discord.controller;

import java.util.List;

import org.kaschka.fersagers.discord.database.dao.SoundBoardDAO;
import org.kaschka.fersagers.discord.database.model.Sound;
import org.kaschka.fersagers.discord.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
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

    @GetMapping("/guild/{guildId}/sound/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Sound getSound(@PathVariable long guildId, @PathVariable String name) {
        Sound sound = soundBoardDAO.getSound(guildId, name);
        if (sound == null || StringUtils.isEmpty(sound.getUrl())) {
            throw new NotFoundException("Sound not Found");
        }
        return sound;
    }

    @GetMapping("/guild/{guildId}/sound")
    @ResponseStatus(HttpStatus.OK)
    public List<Sound> getSound(@PathVariable long guildId) {
        return soundBoardDAO.getAll(guildId);
    }

    @DeleteMapping("/guild/{guildId}/sound/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSound(@PathVariable long guildId, @PathVariable String name) {
        soundBoardDAO.removeSound(guildId, name);
    }

    @PostMapping("/guild/sound")
    @ResponseStatus(HttpStatus.CREATED)
    public void setSound(@RequestBody Sound sound) {
        soundBoardDAO.setSound(sound);
    }
}
