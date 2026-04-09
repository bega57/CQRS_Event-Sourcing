package at.fhv.se.systemarchitectures.cqrs.infrastructure;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;


/**
 * Example JPA entity defined as a Panache Entity.
 * An ID field of Long type is provided, if you want to define your own ID field extends <code>PanacheEntityBase</code> instead.
 *
 * This uses the active record pattern, you can also use the repository pattern instead:
 * {@see https://quarkus.io/guides/hibernate-orm-panache#solution-2-using-the-repository-pattern}.
 *
 * Usage:
 *
 * {@code
 *     public void doSomething() {
 *         DemoEntity entity1 = new DemoEntity();
 *         entity1.field = "field-1";
 *         entity1.persist();
 *
 *         List<DemoEntity> entities = DemoEntity.listAll();
 *     }
 * }
 */
@Entity
public class DemoEntity extends PanacheEntity {
    public String field;
}
