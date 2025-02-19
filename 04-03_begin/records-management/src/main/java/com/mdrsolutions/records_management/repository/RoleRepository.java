package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
