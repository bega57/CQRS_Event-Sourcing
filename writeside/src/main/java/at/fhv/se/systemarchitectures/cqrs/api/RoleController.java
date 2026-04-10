package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.application.RoleService;
import at.fhv.se.systemarchitectures.cqrs.infrastructure.RoleEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/roles")
public class RoleController {

    @Inject
    RoleService roleService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createRole(RoleRequest request) {
        roleService.createRole(request.id);
    }
}