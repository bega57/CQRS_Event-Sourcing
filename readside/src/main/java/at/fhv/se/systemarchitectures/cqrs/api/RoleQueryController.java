package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.readmodel.RolePermissionRepository;
import at.fhv.se.systemarchitectures.cqrs.readmodel.RolePermissionEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
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