package at.fhv.se.systemarchitectures.cqrs.domain;

import java.util.HashSet;
import java.util.Set;

public class Role {

    private String id;
    private Set<String> permissions = new HashSet<>();

    public Role(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }
}