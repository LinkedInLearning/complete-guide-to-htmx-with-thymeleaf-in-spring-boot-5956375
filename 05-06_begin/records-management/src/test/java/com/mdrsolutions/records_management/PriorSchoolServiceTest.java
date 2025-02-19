package com.mdrsolutions.records_management;

import com.mdrsolutions.records_management.entity.PriorSchool;
import com.mdrsolutions.records_management.service.PriorSchoolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class PriorSchoolServiceTest {

    @Autowired
    private PriorSchoolService priorSchoolService;

    @Test
    public void testFindMostRecentPriorSchool() {
        Long studentId = 1L; // Replace with a valid student ID

        Optional<PriorSchool> mostRecentSchool = priorSchoolService.findMostRecentSchool(studentId);

        if (mostRecentSchool.isPresent()) {
            PriorSchool priorSchool = mostRecentSchool.get();
            System.out.println("Most recent prior school for student ID " + studentId + ":");
            System.out.println("School Name: " + priorSchool.getSchoolName());
            System.out.println("Date Last Attended: " + priorSchool.getDateLastAttended());
        } else {
            System.out.println("No prior schools found for student ID " + studentId);
        }
    }
}
