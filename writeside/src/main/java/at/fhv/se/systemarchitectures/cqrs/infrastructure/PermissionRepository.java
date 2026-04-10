package at.fhv.se.systemarchitectures.cqrs.infrastructure;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PermissionRepository implements PanacheRepository<PermissionEntity> {
}