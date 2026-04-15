package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.application.RoleService;
import at.fhv.se.systemarchitectures.cqrs.infrastructure.RoleEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/roles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleController {

    @Inject
    RoleService roleService;

    @POST
    public Response createRole(RoleRequest request) {
        try {
            roleService.createRole(request.id);
            return Response.ok("Role created").build();

        } catch (WebApplicationException e) {
            return Response.status(e.getResponse().getStatus())
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteRole(@PathParam("id") String id) {
        roleService.deleteRole(id);
    }

    @POST
    @Path("/{id}/permissions")
    public void addPermission(@PathParam("id") String id, PermissionRequest request) {
        roleService.addPermission(id, request.permission);
    }

    @DELETE
    @Path("/{id}/permissions/{permission}")
    public void removePermission(@PathParam("id") String id,
                                 @PathParam("permission") String permission) {
        roleService.removePermission(id, permission);
    }

    @POST
    @Path("/{id}/children")
    public void addChildRole(@PathParam("id") String parentId,
                             RoleAssignmentRequest request) {
        roleService.assignRole(parentId, request.childRoleId);
    }

    @DELETE
    @Path("/{id}/children/{childId}")
    public void removeChildRole(@PathParam("id") String parentId,
                                @PathParam("childId") String childId) {
        roleService.removeChildRole(parentId, childId);
    }

    @OPTIONS
    @Path("/{any: .*}")
    public void corsPreflight() {
    }
}