package org.kaschka.fersagers.discord.database.dao;

import org.kaschka.fersagers.discord.database.model.Guild;
import org.springframework.data.repository.CrudRepository;

public interface GuildRepository extends CrudRepository<Guild, Integer> {
    Guild getById(long id);
}
