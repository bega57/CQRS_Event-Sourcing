package at.fhv.se.systemarchitectures.cqrs.readmodel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RoleRelationEntity {

    @Id
    public String id;

    public String parentRoleId;
    public String childRoleId;
}
