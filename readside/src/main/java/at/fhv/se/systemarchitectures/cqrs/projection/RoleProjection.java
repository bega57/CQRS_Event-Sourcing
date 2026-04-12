package at.fhv.se.systemarchitectures.cqrs.projection;

import at.fhv.se.systemarchitectures.cqrs.infrastructure.RolePermissionRepository;
import at.fhv.se.systemarchitectures.cqrs.readmodel.RolePermissionEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RoleProjection {

    @Inject
    RolePermissionRepository repository;

    @Transactional
    public void handle(String event) {

        String eventType = event.replaceAll(".*\"eventType\":\"(.*?)\".*", "$1");

        switch (eventType) {

            case "PermissionAddedEvent":
                handlePermissionAdded(event);
                break;

            case "PermissionRemovedEvent":
                handlePermissionRemoved(event);
                break;

            case "RoleAssignedEvent":
                handleRoleAssigned(event);
                break;

            case "RoleUnassignedEvent":
                handleRoleUnassigned(event);
                break;

            case "RoleDeletedEvent":
                handleRoleDeleted(event);
                break;

            default:
                System.out.println("Unhandled event: " + eventType);
        }
    }

    private void handlePermissionAdded(String event) {

        String roleId = event.replaceAll(".*\"roleId\":\"(.*?)\".*", "$1");
        String permission = event.replaceAll(".*\"permission\":\"(.*?)\".*", "$1");

        String id = roleId + "_" + permission;

        if (repository.find("id", id).firstResult() != null) return;

        RolePermissionEntity entry = new RolePermissionEntity();
        entry.id = id;
        entry.roleId = roleId;
        entry.permission = permission;

        repository.persist(entry);
    }

    private void handlePermissionRemoved(String event) {

        String roleId = event.replaceAll(".*\"roleId\":\"(.*?)\".*", "$1");
        String permission = event.replaceAll(".*\"permission\":\"(.*?)\".*", "$1");

        repository.delete(roleId + "_" + permission);
    }

    private void handleRoleAssigned(String event) {

        String parent = event.split("\"parentRoleId\":\"")[1].split("\"")[0];
        String child = event.split("\"childRoleId\":\"")[1].split("\"")[0];

        var childPermissions = repository.findByRoleId(child);

        for (var perm : childPermissions) {

            String id = parent + "_" + perm.permission;

            if (repository.find("id", id).firstResult() != null) continue;

            RolePermissionEntity entry = new RolePermissionEntity();
            entry.id = id;
            entry.roleId = parent;
            entry.permission = perm.permission;

            repository.persist(entry);
        }
    }

    private void handleRoleUnassigned(String event) {

        String parent = event.split("\"parentRoleId\":\"")[1].split("\"")[0];
        String child = event.split("\"childRoleId\":\"")[1].split("\"")[0];

        var childPermissions = repository.findByRoleId(child);

        for (var perm : childPermissions) {
            repository.delete(parent + "_" + perm.permission);
        }
    }

    private void handleRoleDeleted(String event) {

        String roleId = event.replaceAll(".*\"roleId\":\"(.*?)\".*", "$1");

        repository.delete("roleId", roleId);
    }
}