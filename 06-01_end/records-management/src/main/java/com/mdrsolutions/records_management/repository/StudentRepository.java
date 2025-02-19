package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByGuardians_PersonId(Long personId);

}
