package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
}
