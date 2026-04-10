package at.fhv.se.systemarchitectures.cqrs.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PermissionEntity {
    @Id
    @GeneratedValue
    public Long id;

    public String roleId;
    public String permission;
}
