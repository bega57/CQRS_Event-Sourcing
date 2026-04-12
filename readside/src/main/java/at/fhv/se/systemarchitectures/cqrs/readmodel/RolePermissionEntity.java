package at.fhv.se.systemarchitectures.cqrs.readmodel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

    @Entity
    public class RolePermissionEntity {

        @Id
        public String id; // roleId + "_" + permission

        public String roleId;
        public String permission;
    }

