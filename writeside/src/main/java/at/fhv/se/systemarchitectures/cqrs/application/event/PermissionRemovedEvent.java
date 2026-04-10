package at.fhv.se.systemarchitectures.cqrs.application.event;

public class PermissionRemovedEvent {

    public String roleId;
    public String permission;

    public PermissionRemovedEvent() {}

    public PermissionRemovedEvent(String roleId, String permission) {
        this.roleId = roleId;
        this.permission = permission;
    }
}