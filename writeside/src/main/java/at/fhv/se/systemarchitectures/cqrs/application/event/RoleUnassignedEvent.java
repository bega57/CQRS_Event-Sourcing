package at.fhv.se.systemarchitectures.cqrs.application.event;

public class RoleUnassignedEvent {

    public String parentRoleId;
    public String childRoleId;

    public long timestamp;
    public String eventType;

    public RoleUnassignedEvent() {}

    public RoleUnassignedEvent(String parentRoleId, String childRoleId) {
        this.parentRoleId = parentRoleId;
        this.childRoleId = childRoleId;
        this.timestamp = System.currentTimeMillis();
        this.eventType = "RoleUnassignedEvent";
    }
}