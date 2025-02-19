package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    void findByUserEmail(String email);

    @Query("SELECT p FROM Person p JOIN p.emails e "
            + "WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :term, '%')) "
            + "   OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :term, '%')) "
            + "   OR LOWER(e.emailAddress) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Person> searchByNameOrEmail(@Param("term") String term);
}
