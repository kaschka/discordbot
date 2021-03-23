package org.kaschka.fersagers.discord.database.dao;

import org.kaschka.fersagers.discord.database.model.Sound;
import org.springframework.stereotype.Component;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;

@Component
public class SoundBoardDAO {

    private final SoundBoardRepository soundBoardRepository;

    public SoundBoardDAO(SoundBoardRepository soundBoardRepository) {
        this.soundBoardRepository = soundBoardRepository;
    }

    public void setSound(Sound sound) {
        if (!soundBoardRepository.existsByGuildIdAndName(sound.getGuildId(), sound.getName())) {
            soundBoardRepository.save(sound);
        } else {
            throw new KeyAlreadyExistsException("The combination of guild and name already exists!");
        }
    }

    public List<Sound> getAll(long guildId) {
        return soundBoardRepository.getByGuildId(guildId);
    }

    public Sound getSound(long guildId, String id) {
        return soundBoardRepository.getByGuildIdAndName(guildId, id);
    }

    public void removeSound(long guildId, String id) {
        soundBoardRepository.removeByGuildIdAndName(guildId, id);
    }

}
