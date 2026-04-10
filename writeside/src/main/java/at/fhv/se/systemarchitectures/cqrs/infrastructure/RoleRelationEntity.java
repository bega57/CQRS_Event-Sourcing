package at.fhv.se.systemarchitectures.cqrs.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RoleRelationEntity {
    @Id
    @GeneratedValue
    public Long id;

    public String parentRoleId;
    public String childRoleId;
}
