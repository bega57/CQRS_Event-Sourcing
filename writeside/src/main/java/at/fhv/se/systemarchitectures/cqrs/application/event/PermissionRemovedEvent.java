package at.fhv.se.systemarchitectures.cqrs.application.event;

public class PermissionRemovedEvent {

    public String roleId;
    public String permission;

    public long timestamp;
    public String eventType;

    public PermissionRemovedEvent() {}

    public PermissionRemovedEvent(String roleId, String permission) {
        this.roleId = roleId;
        this.permission = permission;
        this.timestamp = System.currentTimeMillis();
        this.eventType = "PermissionRemovedEvent";
    }
}