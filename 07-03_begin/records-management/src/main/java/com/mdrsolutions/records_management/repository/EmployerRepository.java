package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    List<Employer> findByPerson_PersonId(Long personId);
}
