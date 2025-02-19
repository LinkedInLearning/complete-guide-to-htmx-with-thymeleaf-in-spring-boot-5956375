package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.entity.PhoneNumber;
import com.mdrsolutions.records_management.repository.PhoneNumberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneNumberService {
    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumberService(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    public Optional<PhoneNumber> getPhoneNumberById(Long phoneId) {
        return phoneNumberRepository.findById(phoneId);
    }

    public void saveOrUpdatePhone(Person person, PhoneNumber phoneNumber) {
        phoneNumber.setPerson(person);
        phoneNumberRepository.save(phoneNumber);
    }

    public List<PhoneNumber> findPhonesByPersonId(Long personId){
        return phoneNumberRepository.findByPersonPersonId(personId);
    }

    public void deletePhone(Long phoneId){
        phoneNumberRepository.deleteById(phoneId);
    }

    public boolean isDuplicatePhoneNumberForPerson(Person person, String number) {
        Optional<PhoneNumber> optionalPhoneNumber = phoneNumberRepository.findByNumber(number);
        return optionalPhoneNumber.map(phoneNumber ->
                phoneNumber.getNumber().equalsIgnoreCase(number)
        ).orElse(false);
    }

}
