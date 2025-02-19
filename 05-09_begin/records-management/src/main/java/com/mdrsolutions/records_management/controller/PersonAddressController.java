package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import com.mdrsolutions.records_management.controller.dto.PersonAddressDto;
import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.service.CheckPersonMissingFieldService;
import com.mdrsolutions.records_management.service.PersonAddressService;
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

@Controller
public class PersonAddressController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonAddressController.class);
    private final PersonService personService;
    private final PersonAddressService personAddressService;
    private final CheckPersonMissingFieldService missingFieldService;

    public PersonAddressController(PersonService personService, PersonAddressService personAddressService, CheckPersonMissingFieldService missingFieldService) {
        this.personService = personService;
        this.personAddressService = personAddressService;
        this.missingFieldService = missingFieldService;
    }

    //person address section
    @GetMapping("/person/{personId}/person-address/add")
    public String showAddPersonAddressForm(@PathVariable("personId") Long personId,
                                           Model model) {
        LOGGER.info("showAddPersonAddressForm(...) - personId:{}", personId);
        PersonAddressDto personAddressDto = personAddressService.createNewPersonAddressObject(personId);

        model.addAttribute("addressDto", personAddressDto);
        model.addAttribute("personId", personId);
        model.addAttribute("newAddress", true);

        return "person/modify/editable-person-address-form :: address-form";
    }

    @GetMapping("/person/{personId}/person-address/edit/{addressId}")
    public String showEditPersonAddressForm(@PathVariable("personId") Long personId,
                                            @PathVariable("addressId") Long addressId,
                                            Model model) {

        LOGGER.info("showEditPersonAddressForm(...) - personId:{}, addressId:{}", personId, addressId);

        PersonAddressDto personAddressDto = personAddressService.getPersonAddressDtoById(addressId);

        model.addAttribute("addressDto", personAddressDto);
        model.addAttribute("addressId", addressId);
        model.addAttribute("newAddress", false);

        return "person/modify/editable-person-address-form :: address-form";

    }

    @PostMapping("/person/{personId}/person-address/save")
    public View savePersonAddress(@ModelAttribute PersonAddressDto personAddressDto,
                                  @PathVariable("personId") Long personId,
                                  Model model) {
        // Verify that 'personAddress' here contains the ID correctly and not the email string.
        LOGGER.info("Saving phone for personId: {}, addressId: {}", personId, personAddressDto.addressId());

        Person person = personService.getPersonById(personId);
        PersonAddressDto newPersonAddressDto = personAddressService.saveOrUpdatePersonAddress(person, personAddressDto);

        //getting updated person object after updating personAddress
        person = personService.getPersonById(personId);

        //checking for review messages
        MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(person);

        //add check for missingDetails on Address

        //regular model attributes
        model.addAttribute("person", person);
        model.addAttribute("addressDto", newPersonAddressDto);
        model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
        model.addAttribute("missingDetailsList", missingDetailsDto.getMissingFields());

        //add model attribute for afterSave

        //determine message alert types to be returned in model attributes

        return FragmentsRendering
                .with("person/person-address-item :: address-item")
                .fragment("person/person-mark-for-review :: mark-for-review-info")
                .build();
    }


    @DeleteMapping("/person/{personId}/person-address/delete/{addressId}")
    public RedirectView deletePersonAddress(@PathVariable("personId") Long personId, @PathVariable("addressId") Long addressId, RedirectAttributes redirectAttributes) {
        LOGGER.info("deletePersonAddress(...) - addressId {}", addressId);
        personAddressService.deletePersonAddress(addressId);
        redirectAttributes.addFlashAttribute("successMessage", "Person Address Deleted!");
        RedirectView redirectView = new RedirectView("/person/view/" + personId);
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        LOGGER.info("completed call to person address");
        return redirectView;
    }
}
