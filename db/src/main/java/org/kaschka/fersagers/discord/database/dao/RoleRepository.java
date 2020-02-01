package org.kaschka.fersagers.discord.database.dao;

import org.kaschka.fersagers.discord.database.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
