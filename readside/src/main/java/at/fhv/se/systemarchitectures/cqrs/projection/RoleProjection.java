package at.fhv.se.systemarchitectures.cqrs.projection;

import at.fhv.se.systemarchitectures.cqrs.readmodel.RolePermissionRepository;
import at.fhv.se.systemarchitectures.cqrs.readmodel.RolePermissionEntity;
import at.fhv.se.systemarchitectures.cqrs.readmodel.RoleRelationEntity;
import at.fhv.se.systemarchitectures.cqrs.readmodel.RoleRelationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RoleProjection {

    @Inject
    RolePermissionRepository repository;

    @Inject
    RoleRelationRepository relationRepo;

    @ConfigProperty(name = "filter.prefix", defaultValue = "")
    String prefix;

    @Transactional
    public void handle(String event) {

        String id = event.contains("\"roleId\"")
                ? event.replaceAll(".*\"roleId\":\"(.*?)\".*", "$1")
                : event.contains("\"id\"")
                ? event.replaceAll(".*\"id\":\"(.*?)\".*", "$1")
                : null;

        if (id != null && prefix != null && !prefix.isBlank()) {
            if (!id.toLowerCase().startsWith(prefix.toLowerCase())) {
                System.out.println("Filtered event for id: " + id);
                return;
            }
        }

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

        repository.delete("id", roleId + "_" + permission);
    }

    private void handleRoleAssigned(String event) {

        // DO NOTHING
    }

    private void handleRoleUnassigned(String event) {

        // DO NOTHING
    }

    private void handleRoleDeleted(String event) {

        String roleId = event.replaceAll(".*\"roleId\":\"(.*?)\".*", "$1");

        repository.delete("roleId", roleId);
    }
}