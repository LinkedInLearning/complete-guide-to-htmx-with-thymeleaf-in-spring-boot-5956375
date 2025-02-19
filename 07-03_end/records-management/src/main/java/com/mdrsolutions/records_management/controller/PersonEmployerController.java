package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import com.mdrsolutions.records_management.entity.Employer;
import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.service.CheckPersonMissingFieldService;
import com.mdrsolutions.records_management.service.EmployerService;
import com.mdrsolutions.records_management.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.FragmentsRendering;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Controller
public class PersonEmployerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonEmployerController.class);
    private final PersonService personService;
    private final EmployerService employerService;
    private final CheckPersonMissingFieldService missingFieldService;


    public PersonEmployerController(PersonService personService, EmployerService employerService, CheckPersonMissingFieldService missingFieldService) {
        this.personService = personService;
        this.employerService = employerService;
        this.missingFieldService = missingFieldService;
    }


    //employers-info
    @GetMapping("/person/{personId}/employer/add")
    public String showAddEmployerForm(@PathVariable("personId") Long personId, Model model) {
        model.addAttribute("employer", new Employer());
        model.addAttribute("personId", personId);
        return "person/modify/editable-employers-form :: employer-form";
    }

    @GetMapping("/person/{personId}/employer/edit/{employerId}")
    public String showEditEmployerForm(@PathVariable("personId") Long personId,
                                       @PathVariable("employerId") Long employerId, Model model) {
        Optional<Employer> employer = employerService.getEmployerById(employerId);
        if (employer.isPresent()) {
            model.addAttribute("employer", employer.get());
            model.addAttribute("employerId", employerId);
            return "person/modify/editable-employers-form :: employer-form";
        }
        model.addAttribute("errorMessage", "Employer does not exist");
        return "person/modify/editable-employers-form :: employer-form";
    }

    @PostMapping("/person/{personId}/employer/save")
    public View saveEmployer(@ModelAttribute Employer employer,
                             @PathVariable("personId") Long personId,
                             Model model
    ) {
        // Verify that 'personAddress' here contains the ID correctly and not the email string.
        LOGGER.info("Saving phone for personId: {}, employerId: {}", personId, employer.getEmployerId());
        Person person = personService.getPersonById(personId);
        employerService.saveOrUpdateEmployer(person, employer);
        List<Employer> employers = employerService.findEmployersByPersonId(personId);

        //getting updated person object after updating personAddress
        person = personService.getPersonById(personId);

        //checking for review messages
        MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(person);
        model.addAttribute("person", person);
        model.addAttribute("employers", employers);
        model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
        model.addAttribute("missingDetailsList", missingDetailsDto.getMissingFields());

        LOGGER.info("about to return fragments for person employer save method");
        return FragmentsRendering
                .with("person/employers-info :: employers-info")
                .fragment("person/person-mark-for-review :: mark-for-review-info")
                .build();
    }


    @DeleteMapping("/person/{personId}/employer/delete/{addressId}")
    public RedirectView deleteEmployer(@PathVariable("personId") Long personId, @PathVariable("employerId") Long employerId, RedirectAttributes redirectAttributes) {
        LOGGER.info("deleteEmployer(...) - employerId {}", employerId);
        employerService.deleteEmployer(employerId);
        redirectAttributes.addFlashAttribute("successMessage", "Employer Deleted!");
        RedirectView redirectView = new RedirectView("/person/view/" + personId);
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        LOGGER.info("completed call to Employer");
        return redirectView;
    }
}
