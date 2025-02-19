package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.repository.PersonRepository;
import com.mdrsolutions.records_management.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/person/{personId}")
    public String getPersonDetails(@PathVariable("personId") Long personId, Model model){
        LOGGER.info("getPersonDetails(...)");
        Person person = personService.getPersonById(personId);
        model.addAttribute("person", person);
        return "dashboard/person-info :: personal-info";
    }

}
