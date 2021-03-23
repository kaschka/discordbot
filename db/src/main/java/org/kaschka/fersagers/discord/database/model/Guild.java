package org.kaschka.fersagers.discord.database.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "guild")
public class Guild {

    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "musicChannel", nullable = true)
    private long musicChannel;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "guildId")
    private List<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public long getMusicChannel() {
        return musicChannel;
    }

    public void setMusicChannel(long musicChannel) {
        this.musicChannel = musicChannel;
    }
}
