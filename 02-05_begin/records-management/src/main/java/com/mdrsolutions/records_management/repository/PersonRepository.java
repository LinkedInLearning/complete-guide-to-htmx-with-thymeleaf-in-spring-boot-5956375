package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
