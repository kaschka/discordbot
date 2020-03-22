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

    public List<Sound> getAll() {
        return soundBoardRepository.getAllBy();
    }

    public Sound getSound(String id) {
        return soundBoardRepository.getById(id);
    }

    public void removeSound(String id) {
        soundBoardRepository.removeById(id);
    }

}
