package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByEmailAddress(String emailAddress);
}
