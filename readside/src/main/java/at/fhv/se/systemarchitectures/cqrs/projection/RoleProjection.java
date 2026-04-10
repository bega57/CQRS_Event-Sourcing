package at.fhv.se.systemarchitectures.cqrs.projection;

import at.fhv.se.systemarchitectures.cqrs.readmodel.RoleEntity;
import at.fhv.se.systemarchitectures.cqrs.readmodel.RoleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RoleProjection {

    @Inject
    RoleRepository repository;

    @Transactional
    public void handle(String event) {
        try {
            if (!event.contains("id")) {
                System.out.println("Invalid event: " + event);
                return;
            }

            String id = event.replaceAll(".*\"id\":\"(.*?)\".*", "$1");

            if (repository.findById(id) == null) {

                RoleEntity role = new RoleEntity();
                role.id = id;

                repository.persist(role);

                System.out.println("Saved to ReadDB: " + id);

            } else {
                System.out.println("Role already exists: " + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}