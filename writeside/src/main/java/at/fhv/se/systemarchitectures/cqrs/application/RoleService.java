package at.fhv.se.systemarchitectures.cqrs.application;

import at.fhv.se.systemarchitectures.cqrs.infrastructure.RoleEntity;
import at.fhv.se.systemarchitectures.cqrs.infrastructure.RoleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RoleService {

    @Inject
    RoleRepository repository;

    @Transactional
    public void createRole(String id) {
        RoleEntity entity = new RoleEntity();
        entity.id = id;

        repository.persist(entity);
    }
}