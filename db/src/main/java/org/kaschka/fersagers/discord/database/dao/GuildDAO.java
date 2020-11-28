package org.kaschka.fersagers.discord.database.dao;

import java.util.List;

import org.kaschka.fersagers.discord.controller.to.GuildTO;
import org.kaschka.fersagers.discord.database.model.Guild;
import org.kaschka.fersagers.discord.database.model.Role;
import org.kaschka.fersagers.discord.exception.GuildAlreadyExistsException;
import org.kaschka.fersagers.discord.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

@Component
public class GuildDAO {

    private final GuildRepository guildRepository;

    @Autowired
    public GuildDAO(GuildRepository guildRepository, RoleDAO roleDAO) {
        this.guildRepository = guildRepository;
    }

    public void addGuild(GuildTO guildTO) {
        Guild guild = new Guild();
        guild.setId(guildTO.getId());
        guild.setMusicChannel(guildTO.getMusicChannel());

        try {
            guildRepository.save(guild);
        } catch (JpaSystemException e) {
            //guild exists
            throw new GuildAlreadyExistsException();
        }
    }

    public Guild getGuild(long id) {
        Guild guild = guildRepository.getById(id);
        if (guild != null) {
            return guild;
        }
        throw new NotFoundException("guild.not.found");
    }

    public List<Role> getRole(long id) {
        return getGuild(id).getRoles();
    }

    public long getMusicChannel(long id) {
        return getGuild(id).getMusicChannel();
    }
}
