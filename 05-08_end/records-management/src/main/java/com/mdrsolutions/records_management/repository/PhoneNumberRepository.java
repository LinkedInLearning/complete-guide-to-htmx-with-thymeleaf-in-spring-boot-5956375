package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    Optional<PhoneNumber> findByNumber(String number);
}
