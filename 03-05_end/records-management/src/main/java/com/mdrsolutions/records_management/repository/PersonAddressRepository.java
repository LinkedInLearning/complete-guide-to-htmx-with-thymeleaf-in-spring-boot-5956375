package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.PersonAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonAddressRepository extends JpaRepository<PersonAddress, Long> {
    List<PersonAddress> findByPerson_PersonId(Long personId);
}
