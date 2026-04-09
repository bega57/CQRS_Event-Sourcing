package at.fhv.se.systemarchitectures.cqrs.readmodel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class DemoReadModelEntity {

    @Id
    private String field;


    public DemoReadModelEntity() {

    }

    public DemoReadModelEntity(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
