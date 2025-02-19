package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
