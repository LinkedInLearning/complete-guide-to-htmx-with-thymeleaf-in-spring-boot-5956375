package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.PersonDoesNotExistException;
import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person getPersonById(final Long personId){
        Optional<Person> person = personRepository.findById(personId);
        if(person.isPresent()){
            return person.get();
        } else {
            throw new PersonDoesNotExistException("Person with ID " + personId + " does not exist");
        }
    }

    public void savePerson(Person existingPerson) {
        personRepository.save(existingPerson);
    }
}
