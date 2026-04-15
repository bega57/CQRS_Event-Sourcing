package at.fhv.se.systemarchitectures.cqrs.readmodel;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRelationRepository implements PanacheRepository<RoleRelationEntity> {
}