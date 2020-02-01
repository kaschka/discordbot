package org.kaschka.fersagers.discord.database.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleDAO {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleDAO(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }
}
