package at.fhv.se.systemarchitectures.cqrs.readmodel;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class DemoRepository implements PanacheRepository<DemoReadModelEntity> {


    public List<DemoReadModelEntity> loadAll() {
        return findAll().stream().toList();
    }

    @Startup // <-- quarkus invokest this method after application startup
    @Transactional
    public void insertDemoData() {
        List.of(new DemoReadModelEntity("foo"), new DemoReadModelEntity("bar"), new DemoReadModelEntity("foobar")).forEach(entity -> {
            boolean alreadyExisting = find("field", entity.getField()).firstResultOptional().isPresent();
            if (!alreadyExisting) {
                persist(entity);
            }
        });
    }

}
