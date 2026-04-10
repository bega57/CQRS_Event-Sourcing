package at.fhv.se.systemarchitectures.cqrs.readmodel;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RoleEntity extends PanacheEntityBase {

    @Id
    public String id;
}