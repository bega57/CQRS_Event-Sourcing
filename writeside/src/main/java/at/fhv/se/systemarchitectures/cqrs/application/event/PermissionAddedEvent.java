package at.fhv.se.systemarchitectures.cqrs.application.event;

public class PermissionAddedEvent {

    public String roleId;
    public String permission;

    public PermissionAddedEvent() {
    }

    public PermissionAddedEvent(String roleId, String permission) {
        this.roleId = roleId;
        this.permission = permission;
    }
}