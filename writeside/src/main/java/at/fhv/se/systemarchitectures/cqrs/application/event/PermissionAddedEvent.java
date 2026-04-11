package at.fhv.se.systemarchitectures.cqrs.application.event;

public class PermissionAddedEvent {

    public String roleId;
    public String permission;

    public long timestamp;
    public String eventType;

    public PermissionAddedEvent() {}

    public PermissionAddedEvent(String roleId, String permission) {
        this.roleId = roleId;
        this.permission = permission;
        this.timestamp = System.currentTimeMillis();
        this.eventType = "PermissionAddedEvent";
    }
}