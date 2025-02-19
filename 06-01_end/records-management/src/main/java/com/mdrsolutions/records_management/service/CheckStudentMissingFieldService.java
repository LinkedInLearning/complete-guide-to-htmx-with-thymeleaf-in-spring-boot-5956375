package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import com.mdrsolutions.records_management.entity.*;
import com.mdrsolutions.records_management.util.CheckMissingDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CheckStudentMissingFieldService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckStudentMissingFieldService.class);

    private MissingDetailsDto master = new MissingDetailsDto(0, new ArrayList<>());

    public MissingDetailsDto checkForMissingFields(Student student) {
        LOGGER.info("checkForMissingFields(...)");
        //resetting at the begin of this call.
        master = new MissingDetailsDto(0, new ArrayList<>());

        //checking for missing details on prior schools
        Set<PriorSchool> priorSchools = student.getPriorSchools();
        if(!priorSchools.isEmpty()){
            LOGGER.info("schools are not empty");
            for(PriorSchool school : priorSchools){
                sumUpMissingDetails(checkForDetails(school));
            }
        } else {
            LOGGER.info("Adding mark for review about missing schools");
            MissingDetailsDto missingDetailsDto = new MissingDetailsDto(1L, List.of("You are required to list at least one previous school your student attended!"));
            sumUpMissingDetails(missingDetailsDto);
        }

        //checking for missing details on student object
        MissingDetailsDto missingStudentDetails = checkForDetails(student);
        sumUpMissingDetails(missingStudentDetails);

        return master;
    }

    private void sumUpMissingDetails(MissingDetailsDto missingDetailsDto) {
        master.getMissingFields().addAll(missingDetailsDto.getMissingFields());
        master.setMissingCount(master.getMissingCount() + missingDetailsDto.getMissingCount());
    }

    private <E> MissingDetailsDto checkForDetails(E entity){
        CheckMissingDetails<E> missingDetails = new CheckMissingDetails<>(entity);
        return missingDetails.getMissingDetails();
    }
}
