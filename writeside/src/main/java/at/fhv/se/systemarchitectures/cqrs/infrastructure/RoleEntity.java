package at.fhv.se.systemarchitectures.cqrs.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RoleEntity {

    @Id
    public String id;
}