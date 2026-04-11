package at.fhv.se.systemarchitectures.cqrs.application.event;

public class RoleDeletedEvent {

    public String roleId;

    public long timestamp;
    public String eventType;

    public RoleDeletedEvent() {}

    public RoleDeletedEvent(String roleId) {
        this.roleId = roleId;
        this.timestamp = System.currentTimeMillis();
        this.eventType = "RoleDeletedEvent";
    }
}
