package at.fhv.se.systemarchitectures.cqrs.application.event;

public class RoleDeletedEvent {

    public String roleId;

    public RoleDeletedEvent() {}

    public RoleDeletedEvent(String roleId) {
        this.roleId = roleId;
    }
}
