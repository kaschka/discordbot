package org.kaschka.fersagers.discord.controller;

import org.kaschka.fersagers.discord.controller.to.GuildTO;
import org.kaschka.fersagers.discord.database.dao.GuildDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuildController {
    private final GuildDAO guildDao;

    @Autowired
    public GuildController(GuildDAO guildDao) {
        this.guildDao = guildDao;
    }

    @PostMapping("/guild")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createGuild(@RequestBody GuildTO guild) {
        guildDao.addGuild(guild);
    }
}
