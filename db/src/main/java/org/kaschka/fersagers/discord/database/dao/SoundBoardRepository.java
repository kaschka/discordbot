package org.kaschka.fersagers.discord.database.dao;

import javax.transaction.Transactional;

import org.kaschka.fersagers.discord.database.model.Sound;
import org.springframework.data.repository.CrudRepository;

public interface SoundBoardRepository extends CrudRepository<Sound, Integer> {
    Sound getById(String id);

    @Transactional
    void removeById(String id);
}
