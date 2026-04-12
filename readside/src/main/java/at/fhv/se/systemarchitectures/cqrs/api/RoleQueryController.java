package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.infrastructure.RolePermissionRepository;
import at.fhv.se.systemarchitectures.cqrs.readmodel.RoleEntity;
import at.fhv.se.systemarchitectures.cqrs.readmodel.RolePermissionEntity;
import at.fhv.se.systemarchitectures.cqrs.readmodel.RoleRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

@Path("/roles")
public class RoleQueryController {

    @Inject
    RolePermissionRepository repository;

    @GET
    public List<RolePermissionEntity> getAll() {
        return repository.listAll();
    }

    @GET
    @Path("/{roleId}/permissions")
    public List<String> getPermissions(@PathParam("roleId") String roleId) {

        return repository.findByRoleId(roleId)
                .stream()
                .map(e -> e.permission)
                .toList();
    }
}