package org.kaschka.fersagers.discord.controller;

import org.kaschka.fersagers.discord.database.dao.GuildDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MusicController {

    private final GuildDAO guildDao;

    @Autowired
    public MusicController(GuildDAO guildDao) {
        this.guildDao = guildDao;
    }

    @GetMapping("/guild/{guildId}/music")
    @ResponseStatus(HttpStatus.OK)
    public long getMusicChannel(@PathVariable long guildId) {
        return guildDao.getMusicChannel(guildId);
    }
}