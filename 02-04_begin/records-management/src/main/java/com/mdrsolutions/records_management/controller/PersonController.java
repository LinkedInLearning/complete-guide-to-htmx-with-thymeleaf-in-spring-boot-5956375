package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.repository.PersonRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public Iterable<Person> findAll(){
        return personRepository.findAll();
    }
}
