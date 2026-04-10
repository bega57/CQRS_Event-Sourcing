package at.fhv.se.systemarchitectures.cqrs.application;

import at.fhv.se.systemarchitectures.cqrs.application.event.RoleCreatedEvent;
import at.fhv.se.systemarchitectures.cqrs.infrastructure.RoleEntity;
import at.fhv.se.systemarchitectures.cqrs.infrastructure.RoleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import at.fhv.se.systemarchitectures.cqrs.publisher.EventBusClient;

@ApplicationScoped
public class RoleService {

    @Inject
    RoleRepository repository;

    @Inject
    @RestClient
    EventBusClient eventBusClient;

    @Transactional
    public void createRole(String id) {
        RoleEntity entity = new RoleEntity();
        entity.id = id;

        repository.persist(entity);

        eventBusClient.publishEvent(new RoleCreatedEvent(id));
    }
}