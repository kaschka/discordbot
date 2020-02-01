package org.kaschka.fersagers.discord.controller;

import java.util.List;

import org.kaschka.fersagers.discord.database.dao.GuildDAO;
import org.kaschka.fersagers.discord.database.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    private final GuildDAO guildDao;

    @Autowired
    public RoleController(GuildDAO guildDao) {
        this.guildDao = guildDao;
    }

    @GetMapping("/guild/{guildId}/role")
    @ResponseStatus(HttpStatus.OK)
    public List<Role> getRole(@PathVariable long guildId) {
        return guildDao.getRole(guildId);
    }
}
