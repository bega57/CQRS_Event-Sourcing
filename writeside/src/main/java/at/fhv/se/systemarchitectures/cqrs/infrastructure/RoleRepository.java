package at.fhv.se.systemarchitectures.cqrs.infrastructure;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class RoleRepository implements PanacheRepositoryBase<RoleEntity, String> {
}