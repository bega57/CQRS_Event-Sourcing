package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.readmodel.RoleEntity;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/roles")
public class RoleQueryController {

    @GET
    public List<RoleEntity> getAll() {
        return RoleEntity.listAll();
    }
}