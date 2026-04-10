package at.fhv.se.systemarchitectures.cqrs.application;

import at.fhv.se.systemarchitectures.cqrs.application.event.*;
import at.fhv.se.systemarchitectures.cqrs.infrastructure.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
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

        if (id == null || id.isBlank()) {
            throw new WebApplicationException("Role id must not be empty", 400);
        }

        if (roleRepository.findById(id) != null) {
            throw new WebApplicationException("Role already exists", 409);
        }

        RoleEntity entity = new RoleEntity();
        entity.id = id;
        roleRepository.persist(entity);

        eventBusClient.publishEvent(new RoleCreatedEvent(id));
    }

    @Transactional
    public void deleteRole(String id) {

        ensureRoleExists(id);

        roleRepository.delete("id = ?1", id);
        permissionRepository.delete("roleId = ?1", id);
        relationRepository.delete("parentRoleId = ?1", id);
        relationRepository.delete("childRoleId = ?1", id);

        eventBusClient.publishEvent(new RoleDeletedEvent(id));
    }

    @Transactional
    public void addPermission(String roleId, String permission) {

        ensureRoleExists(roleId);

        if (permission == null || permission.isBlank()) {
            throw new WebApplicationException("Permission must not be empty", 400);
        }

        boolean exists = permissionRepository
                .find("roleId = ?1 and permission = ?2", roleId, permission)
                .firstResultOptional()
                .isPresent();

        if (exists) {
            throw new WebApplicationException("Permission already exists", 409);
        }

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

        ensureRoleExists(roleId);

        long deleted = permissionRepository.delete(
                "roleId = ?1 and permission = ?2",
                roleId, permission
        );

        if (deleted == 0) {
            throw new WebApplicationException("Permission not found", 404);
        }

        eventBusClient.publishEvent(
                new PermissionRemovedEvent(roleId, permission)
        );
    }

    @Transactional
    public void assignRole(String parentId, String childId) {

        ensureRoleExists(parentId);
        ensureRoleExists(childId);

        if (parentId.equals(childId)) {
            throw new WebApplicationException("Role cannot assign itself", 400);
        }

        boolean exists = relationRepository
                .find("parentRoleId = ?1 and childRoleId = ?2", parentId, childId)
                .firstResultOptional()
                .isPresent();

        if (exists) {
            throw new WebApplicationException("Relation already exists", 409);
        }

        if (createsCycle(parentId, childId)) {
            throw new WebApplicationException("Cycle detected! Assignment not allowed.", 400);
        }

        RoleRelationEntity relation = new RoleRelationEntity();
        relation.parentRoleId = parentId;
        relation.childRoleId = childId;

        relationRepository.persist(relation);

        eventBusClient.publishEvent(
                new RoleAssignedEvent(parentId, childId)
        );
    }

    @Transactional
    public void removeChildRole(String parentId, String childId) {

        ensureRoleExists(parentId);
        ensureRoleExists(childId);

        long deleted = relationRepository.delete(
                "parentRoleId = ?1 and childRoleId = ?2",
                parentId, childId
        );

        if (deleted == 0) {
            throw new WebApplicationException("Relation not found", 404);
        }

        eventBusClient.publishEvent(
                new RoleUnassignedEvent(parentId, childId)
        );
    }

    private void ensureRoleExists(String id) {
        if (roleRepository.findById(id) == null) {
            throw new WebApplicationException("Role not found: " + id, 404);
        }
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