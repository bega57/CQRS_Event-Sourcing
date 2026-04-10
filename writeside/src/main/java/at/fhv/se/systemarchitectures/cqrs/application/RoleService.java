package at.fhv.se.systemarchitectures.cqrs.application;

import at.fhv.se.systemarchitectures.cqrs.application.event.*;
import at.fhv.se.systemarchitectures.cqrs.infrastructure.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import at.fhv.se.systemarchitectures.cqrs.publisher.EventBusClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class RoleService {

    @Inject
    RoleRepository roleRepository;

    @Inject
    PermissionRepository permissionRepository;

    @Inject
    RoleRelationRepository relationRepository;

    @Inject
    @RestClient
    EventBusClient eventBusClient;

    @Transactional
    public void createRole(String id) {
        RoleEntity entity = new RoleEntity();
        entity.id = id;
        roleRepository.persist(entity);

        eventBusClient.publishEvent(new RoleCreatedEvent(id));
    }

    @Transactional
    public void deleteRole(String id) {
        roleRepository.delete("id = ?1", id);
        permissionRepository.delete("roleId = ?1", id);
        relationRepository.delete("parentRoleId = ?1", id);
        relationRepository.delete("childRoleId = ?1", id);

        eventBusClient.publishEvent(new RoleDeletedEvent(id));
    }

    @Transactional
    public void addPermission(String roleId, String permission) {
        PermissionEntity entity = new PermissionEntity();
        entity.roleId = roleId;
        entity.permission = permission;

        permissionRepository.persist(entity);

        eventBusClient.publishEvent(
                new PermissionAddedEvent(roleId, permission)
        );
    }

    @Transactional
    public void removePermission(String roleId, String permission) {
        permissionRepository.delete("roleId = ?1 and permission = ?2", roleId, permission);

        eventBusClient.publishEvent(
                new PermissionRemovedEvent(roleId, permission)
        );
    }

    @Transactional
    public void assignRole(String parentId, String childId) {

        if (parentId.equals(childId)) {
            throw new IllegalArgumentException("Role cannot assign itself");
        }

        if (createsCycle(parentId, childId)) {
            throw new IllegalArgumentException("Cycle detected! Assignment not allowed.");
        }

        RoleRelationEntity relation = new RoleRelationEntity();
        relation.parentRoleId = parentId;
        relation.childRoleId = childId;

        relationRepository.persist(relation);

        eventBusClient.publishEvent(
                new RoleAssignedEvent(parentId, childId)
        );
    }

    private boolean createsCycle(String parentId, String childId) {

        return hasPath(childId, parentId, new HashSet<>());
    }

    private boolean hasPath(String current, String target, Set<String> visited) {

        if (current.equals(target)) {
            return true;
        }

        if (visited.contains(current)) {
            return false;
        }

        visited.add(current);

        List<RoleRelationEntity> children =
                relationRepository.find("parentRoleId", current).list();

        for (RoleRelationEntity rel : children) {
            if (hasPath(rel.childRoleId, target, visited)) {
                return true;
            }
        }

        return false;
    }
}