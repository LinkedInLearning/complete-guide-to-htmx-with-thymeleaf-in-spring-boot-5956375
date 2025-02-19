package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
}
