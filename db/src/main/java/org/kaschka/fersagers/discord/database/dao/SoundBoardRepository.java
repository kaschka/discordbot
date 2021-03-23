package org.kaschka.fersagers.discord.database.dao;

import org.kaschka.fersagers.discord.database.model.Sound;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface SoundBoardRepository extends CrudRepository<Sound, Integer> {
    Sound getByGuildIdAndName(long guildId, String name);

    List<Sound> getByGuildId(long guildId);

    @Transactional
    void removeByGuildIdAndName(long guildId, String name);

    boolean existsByGuildIdAndName(long guildId, String name);

}
