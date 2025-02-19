package com.mdrsolutions.records_management.repository;

import com.mdrsolutions.records_management.entity.PriorSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriorSchoolRepository extends JpaRepository<PriorSchool, Long> {
    List<PriorSchool> findByStudentStudentId(Long studentId);

    @Query(value = "SELECT ps.student_id FROM prior_school ps WHERE ps.id = :priorSchoolId", nativeQuery = true)
    Long findStudentIdByPriorSchoolId(@Param("priorSchoolId") Long priorSchoolId);

    @Query("SELECT ps FROM PriorSchool ps WHERE ps.student.studentId = :studentId " +
            "AND ps.dateLastAttended = (SELECT MAX(ps2.dateLastAttended) FROM PriorSchool ps2 WHERE ps2.student.studentId = :studentId) " +
            "ORDER BY ps.id DESC")
    List<PriorSchool> findMostRecentPriorSchools(@Param("studentId") Long studentId);


    // Order by dateLastAttended in ascending order
    List<PriorSchool> findByStudentStudentIdOrderByDateLastAttendedAsc(Long studentId);

    // Or, order by dateLastAttended in descending order
    List<PriorSchool> findByStudentStudentIdOrderByDateLastAttendedDesc(Long studentId);

}