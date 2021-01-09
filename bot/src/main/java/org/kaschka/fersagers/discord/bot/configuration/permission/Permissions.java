package org.kaschka.fersagers.discord.bot.configuration.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

public class Permissions {
    private List<Role> roles;
    private List<Long> ids;

    public static final Permissions STANDARD_PERMISSIONS;

    static {
        STANDARD_PERMISSIONS = new Permissions();
        STANDARD_PERMISSIONS.addRole(Role.BOT_PERMISSIONS);
    }

    public Permissions() {
        this.roles = new ArrayList<>(0);
        this.ids = new ArrayList<>(0);
        roles.add(Role.EVERYONE);
    }

    public boolean hasPermission(Long id) {
        return ids.contains(id);
    }

    public boolean hasPermission(Role role) {
        return roles.contains(role);
    }

    public static boolean hasPermission(Permissions permissions, Member member) {
        if (member == null) {
            return false;
        }

        boolean role = false;

        if (!permissions.roles.contains(Role.NO_ONE_BUT_ID)) {
            List<String> hasRoles = new ArrayList<>();
            member.getRoles().forEach(e -> hasRoles.add(e.getName()));
            hasRoles.add(Role.EVERYONE.getName());
            List<String> requiredRole = new ArrayList<>();
            permissions.roles.forEach(e -> requiredRole.add(e.getName()));
            role = !CollectionUtils.intersection(hasRoles, requiredRole).isEmpty();
        }

        boolean id = permissions.hasPermission(member.getIdLong());

        return role || id;
    }

    public void addRole(Role role) {
        addRoles(role);
    }

    public void addRoles(Role... roles) {
        if (roles != null) {
            this.roles.remove(Role.EVERYONE);
            this.roles.addAll(Arrays.asList(roles));
        }
    }

    public void addIds(long... ids) {
        if (roles != null) {
            this.roles.remove(Role.EVERYONE);
            this.ids.addAll(Arrays.asList(ArrayUtils.toObject(ids)));
        }
    }
}
