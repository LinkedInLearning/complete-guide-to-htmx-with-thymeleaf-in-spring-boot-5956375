package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import com.mdrsolutions.records_management.entity.*;
import com.mdrsolutions.records_management.util.CheckMissingDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheckPersonMissingFieldService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckPersonMissingFieldService.class);

    private MissingDetailsDto master = new MissingDetailsDto(0, new ArrayList<>());

    public MissingDetailsDto checkForMissingFields(Person person) {
        LOGGER.info("checkForMissingFields(...)");
        //resetting at the begin of this call.
        master = new MissingDetailsDto(0, new ArrayList<>());

        Set<Employer> employers = new HashSet<>(person.getEmployers());
        Set<Email> emails = new HashSet<>(person.getEmails());
        Set<PhoneNumber> phoneNumbers = new HashSet<>(person.getPhoneNumbers());
        Set<PersonAddress> personAddresses = new HashSet<>(person.getAddresses());

        MissingDetailsDto missingPersonDetails = checkForDetails(person);
        sumUpMissingDetails(missingPersonDetails);

        if ( person.getEmploymentStatus() != EmploymentStatus.UNEMPLOYED) {
            MissingDetailsDto missingDetailsDto = null;

            if(employers.isEmpty()){
                missingDetailsDto = new MissingDetailsDto(1L, List.of("Because you selected that you employed/self-employed you must list at least at lease one employer or information about your self-employment."));
            } else {
                for (Employer employer : employers) {
                    missingDetailsDto = checkForDetails(employer);
                }
            }
            sumUpMissingDetails(missingDetailsDto);
        }

        if(!emails.isEmpty()) {
            for (Email email : emails) {
                MissingDetailsDto missingDetails = checkForDetails(email);
                sumUpMissingDetails(missingDetails);
            }
        } else{
            MissingDetailsDto missingDetailsDto = new MissingDetailsDto(1L, List.of("You must list at least one email."));
            sumUpMissingDetails(missingDetailsDto);
        }

        if(!phoneNumbers.isEmpty()) {
            for (PhoneNumber phoneNumber : phoneNumbers) {
                MissingDetailsDto missingDetails = checkForDetails(phoneNumber);
                sumUpMissingDetails(missingDetails);
            }
        } else {
            MissingDetailsDto missingDetailsDto = new MissingDetailsDto(1L, List.of("You must list at least one valid phone number."));
            sumUpMissingDetails(missingDetailsDto);
        }

        if (!personAddresses.isEmpty()) {
            for (PersonAddress personAddress : personAddresses) {
                MissingDetailsDto missingDetailsDto = checkForDetails(personAddress);
                sumUpMissingDetails(missingDetailsDto);
            }
        } else {
            MissingDetailsDto missingDetailsDto = new MissingDetailsDto(1L, List.of("You must list at least one mailing address."));
            sumUpMissingDetails(missingDetailsDto);
        }
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
