package org.kaschka.fersagers.discord.database.dao;

import java.util.List;

import org.kaschka.fersagers.discord.database.model.Sound;
import org.springframework.stereotype.Component;

@Component
public class SoundBoardDAO {

    private final SoundBoardRepository soundBoardRepository;

    public SoundBoardDAO(SoundBoardRepository soundBoardRepository) {
        this.soundBoardRepository = soundBoardRepository;
    }

    public void setSound(Sound sound) {
        soundBoardRepository.save(sound);
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
