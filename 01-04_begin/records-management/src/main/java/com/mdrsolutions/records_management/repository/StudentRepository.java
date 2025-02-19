package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
