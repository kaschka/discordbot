package org.kaschka.fersagers.discord.database.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
