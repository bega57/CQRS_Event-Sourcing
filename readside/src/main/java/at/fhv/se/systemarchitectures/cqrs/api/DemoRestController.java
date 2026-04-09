package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.readmodel.DemoReadModelEntity;
import at.fhv.se.systemarchitectures.cqrs.readmodel.DemoRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/demo")
public class DemoRestController {

    private final DemoRepository repo; // <-- do not inject class in a direct manner like i did here; let the class implement an interface and inject an interface

    public DemoRestController(DemoRepository repo) { // <-- constructor injection
        this.repo = repo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DemoReadModelEntity> getAllDemoEntities() {
        return repo.loadAll();
    }
}
