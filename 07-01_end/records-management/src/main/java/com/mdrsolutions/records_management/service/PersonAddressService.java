package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.controller.dto.PersonAddressDto;
import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.entity.PersonAddress;
import com.mdrsolutions.records_management.repository.PersonAddressRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonAddressService {

    private final PersonAddressRepository personAddressRepository;

    public PersonAddressService(PersonAddressRepository personAddressRepository) {
        this.personAddressRepository = personAddressRepository;
    }

    public List<PersonAddressDto> findPersonAddressDtoListByPersonId(final Long personId){
        List<PersonAddress> personAddressList = findPersonAddressesByPersonId(personId);
        List<PersonAddressDto> personAddressDtoList = new ArrayList<>();

        for(PersonAddress personAddress : personAddressList) {
            personAddressDtoList.add(PersonAddressDto.fromEntity(personAddress));
        }
        return personAddressDtoList;
    }

    public List<PersonAddress> findPersonAddressesByPersonId(final Long personId){
        return personAddressRepository.findByPerson_PersonId(personId);
    }

    public PersonAddressDto getPersonAddressDtoById(Long addressId){
        Optional<PersonAddress> optionalPersonAddress = getPersonAddressById(addressId);
        return PersonAddressDto.fromEntity(optionalPersonAddress.get());
    }
    public Optional<PersonAddress> getPersonAddressById(Long addressId) {
        return personAddressRepository.findById(addressId);
    }

    public PersonAddress saveOrUpdatePersonAddress(Person person, PersonAddress personAddress) {
        personAddress.setPerson(person);
        return personAddressRepository.save(personAddress);
    }

    public void deletePersonAddress(Long addressId) {
        personAddressRepository.deleteById(addressId);
    }

    public PersonAddressDto createNewPersonAddressObject(Long personId) {
        return new PersonAddressDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                personId
        );
    }

    public PersonAddressDto saveOrUpdatePersonAddress(Person person, PersonAddressDto personAddressDto) {
        PersonAddress personAddress = saveOrUpdatePersonAddress( person, transform( personAddressDto,person));
        return PersonAddressDto.fromEntity(personAddress);
    }

    public PersonAddress transform(PersonAddressDto personAddressDto,  Person person){
        PersonAddress personAddress = new PersonAddress();
        personAddress.setPerson(person);
        personAddress.setAddress1(personAddressDto.address1());
        personAddress.setAddress2(personAddressDto.address2());
        personAddress.setCity(personAddressDto.city());
        personAddress.setState(personAddressDto.state());
        personAddress.setZip(personAddressDto.zip());
        personAddress.setAddressType(personAddressDto.addressType());
        personAddress.setCountryCd(personAddressDto.countryCd());

        //logic essentially determines if this will be a new record or not
        if(personAddressDto.addressId() != null){
            personAddress.setAddressId(personAddressDto.addressId());
        } else {
            personAddress.setAddressId(null);
        }

        return personAddress;
    }
}
