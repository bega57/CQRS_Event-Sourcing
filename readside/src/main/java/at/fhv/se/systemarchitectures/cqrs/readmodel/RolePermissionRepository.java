package at.fhv.se.systemarchitectures.cqrs.readmodel;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RolePermissionRepository implements PanacheRepository<RolePermissionEntity> {

    public List<RolePermissionEntity> findByRoleId(String roleId) {
        return list("roleId", roleId);
    }
}