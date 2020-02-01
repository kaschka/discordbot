package org.kaschka.fersagers.discord.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {

    @Id
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
