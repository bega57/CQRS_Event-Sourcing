package at.fhv.se.systemarchitectures.cqrs.application.event;

public class RoleCreatedEvent {
    public String id;

    public RoleCreatedEvent(String id) {
        this.id = id;
    }
}