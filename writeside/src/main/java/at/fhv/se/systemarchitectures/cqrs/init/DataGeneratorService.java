package at.fhv.se.systemarchitectures.cqrs.init;

import at.fhv.se.systemarchitectures.cqrs.application.RoleService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class DataGeneratorService {

    @Inject
    RoleService roleService;

    void onStart(@Observes StartupEvent ev) {

        System.out.println("GENERATING DATA...");

        generate();

        System.out.println("DONE GENERATING");
    }

    private void generate() {

        // roles
        for (int i = 1; i <= 20; i++) {
            int index = i;
            safe(() -> roleService.createRole("Role_" + index));
        }

        // permissions
        for (int i = 1; i <= 20; i++) {
            int index = i;
            safe(() -> roleService.addPermission("Role_" + index, "read"));
            safe(() -> roleService.addPermission("Role_" + index, "write"));
        }

        // role hierarchy
        for (int i = 1; i < 20; i++) {
            int index = i;
            safe(() -> roleService.assignRole("Role_" + index, "Role_" + (index + 1)));
        }

        // users (1000)
        for (int i = 1; i <= 1000; i++) {

            int index = i;
            String userId = "user_" + index;

            safe(() -> roleService.createRole(userId));

            String role = "Role_" + ((index % 20) + 1);

            safe(() -> roleService.assignRole(userId, role));
        }
    }

    private void safe(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
        }
    }
}