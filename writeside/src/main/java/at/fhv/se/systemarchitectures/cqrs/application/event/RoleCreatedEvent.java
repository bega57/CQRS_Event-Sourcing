package at.fhv.se.systemarchitectures.cqrs.application.event;

public class RoleCreatedEvent {

    public String id;

    public long timestamp;
    public String eventType;

    public RoleCreatedEvent() {}

    public RoleCreatedEvent(String id) {
        this.id = id;
        this.timestamp = System.currentTimeMillis();
        this.eventType = "RoleCreatedEvent";
    }
}