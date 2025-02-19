package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import com.mdrsolutions.records_management.entity.*;
import com.mdrsolutions.records_management.repository.PersonRepository;
import com.mdrsolutions.records_management.service.*;
import com.mdrsolutions.records_management.util.CheckMissingDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;
    private final CheckPersonMissingFieldService missingFieldService;
    private final EmailService emailService;
    private final PhoneNumberService phoneNumberService;
    private final PersonAddressService personAddressService;
    private final EmployerService employerService;

    public PersonController(PersonService personService,
                            CheckPersonMissingFieldService missingFieldService,
                            EmailService emailService, PhoneNumberService phoneNumberService, PersonAddressService personAddressService, EmployerService employerService) {
        this.personService = personService;
        this.missingFieldService = missingFieldService;
        this.emailService = emailService;
        this.phoneNumberService = phoneNumberService;
        this.personAddressService = personAddressService;
        this.employerService = employerService;
    }

    @GetMapping("/person/{personId}")
    public String getPersonDetails(@PathVariable("personId") Long personId, Model model) {
        LOGGER.info("getPersonDetails(...)");
        Person person = personService.getPersonById(personId);
        MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(person);
        model.addAttribute("person", person);
        model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
        model.addAttribute("missingDetailsList", missingDetailsDto.getMissingFields());
        return "dashboard/person-info :: personal-info";
    }

    @GetMapping("/person/view/{personId}")
    public String viewPersonFullDetails(@PathVariable("personId") Long personId,
                                        Model model,
                                        HttpServletRequest request) {
        LOGGER.info("viewPersonFullDetails(...) - Loading full details view for person ID: {}", personId);

        Person person = personService.getPersonById(personId);
        MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(person);

        model.addAttribute("person", person);
        model.addAttribute("missingDetailsCount", missingDetailsDto.getMissingCount());
        model.addAttribute("missingDetailsList", missingDetailsDto.getMissingFields());

        boolean isHtmxRequest = request.getHeader("HX-Request") != null;
        if(!isHtmxRequest){
            return "person/person-full-details";
        } else {
            return "person/person-full-details :: person-full-details";
        }
    }

    @GetMapping("/person/edit/{personId}")
    public String editPersonFullDetails(@PathVariable("personId") Long personId, Model model) {
        LOGGER.info("editPersonFullDetails(...) - Loading full details view for person ID: {}", personId);
        Person person = personService.getPersonById(personId);
        MissingDetailsDto missingDetailsDto = missingFieldService.checkForMissingFields(person);
        model.addAttribute("person", person);
        return "person/modify/editable-person-info :: editable-person-info";
    }

    @GetMapping("/person/cancel/{personId}")
    public String cancelPersonInfo(@PathVariable("personId") Long personId, Model model){
        LOGGER.info("viewPersonInfo(...) - returning to the person-info form from cancel");
        Person person = personService.getPersonById(personId);
        model.addAttribute("person", person);

        return "person/person-info :: personal-info";
    }

    @PostMapping("/person/update/{personId}")
    public String updatePersonFullDetails(@PathVariable("personId") Long personId,
                                          @ModelAttribute("person") Person updatedPerson,
                                          Model model) {
        LOGGER.info("updatePersonFullDetails(...) - Updating details for person ID: {}", personId);

        try {
            // Fetch the existing person from the database
            Person existingPerson = personService.getPersonById(personId);

            // Update the details of the existing person with the new values
            existingPerson.setPrefix(updatedPerson.getPrefix());
            existingPerson.setFirstName(updatedPerson.getFirstName());
            existingPerson.setMiddleName(updatedPerson.getMiddleName());
            existingPerson.setLastName(updatedPerson.getLastName());
            existingPerson.setSuffix(updatedPerson.getSuffix());
            existingPerson.setPersonType(updatedPerson.getPersonType());
            existingPerson.setLegalGuardianType(updatedPerson.getLegalGuardianType());

            // Save the updated person back to the database
            personService.savePerson(existingPerson);

            // Add success message
            model.addAttribute("message", "Person details updated successfully.");

            // Redirect to the full details view after a successful update
            return "redirect:/person/view/" + personId;
        } catch (Exception e) {
            LOGGER.error("Error updating person details: {}", e.getMessage());

            // Add error message to the model if there's an issue
            model.addAttribute("errorMessage", "Failed to update person details.");

            // Return the edit page with the error message if something went wrong
            return "person/edit-person-info";
        }
    }

    @GetMapping("/person/{personId}/email/add")
    public String showAddEmailForm(@PathVariable("personId") Long personId, Model model) {
        model.addAttribute("email", new Email());
        model.addAttribute("personId", personId);
        return "person/modify/editable-email-form :: email-form";
    }

    @GetMapping("/person/{personId}/email/edit/{emailId}")
    public String showEditEmailForm(@PathVariable("personId") Long personId,
                                    @PathVariable("emailId") Long emailId, Model model) {
        Optional<Email> emailById = emailService.getEmailById(emailId);// Assuming you have a service to get an email by ID

        if (emailById.isPresent()) {
            model.addAttribute("email", emailById.get());
            model.addAttribute("personId", personId);
            return "person/modify/editable-email-form :: email-form";
        }
        model.addAttribute("errorMessage", "email does not exist");
        return "person/modify/editable-email-form :: email-form";
    }

    @PostMapping("/person/{personId}/email/save")
    public String saveEmail(@ModelAttribute Email email, @PathVariable("personId") Long personId, Model model) {
        // Verify that 'email' here contains the ID correctly and not the email string.
        LOGGER.info("Saving email for personId: {}, emailId: {}", personId, email.getEmailId());

        Person person = personService.getPersonById(personId);
        emailService.saveOrUpdateEmail(person, email);

        model.addAttribute("emails",person.getEmails());
        model.addAttribute("personId", personId);

//        return "redirect:/person/view/" + personId;
        return "person/email-item :: email-item";
    }


    @DeleteMapping("/person/{personId}/email/delete/{emailId}")
    public RedirectView deleteEmail(@PathVariable("personId") Long personId, @PathVariable("emailId") Long emailId, RedirectAttributes redirectAttributes) {
        LOGGER.info("deleteEmail(...) - emailId {}", emailId);
        emailService.deleteEmail(emailId); // Assuming you have a service method for deleting the email
        redirectAttributes.addFlashAttribute("successMessage", "Email Deleted!");
        RedirectView redirectView = new RedirectView("/person/view/" + personId);
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        LOGGER.info("completed call to delete email");
        return redirectView;
    }

    //phones
    @GetMapping("/person/{personId}/phone/add")
    public String showAddPhoneForm(@PathVariable("personId") Long personId, Model model) {
        model.addAttribute("phone", new PhoneNumber());
        model.addAttribute("personId", personId);
        return "person/modify/editable-phone-form :: phone-form";
    }

    @GetMapping("/person/{personId}/phone/edit/{phoneId}")
    public String showEditPhoneForm(@PathVariable("personId") Long personId,
                                    @PathVariable("phoneId") Long phoneId, Model model) {
       // Optional<Email> emailById = emailService.getEmailById(phoneId);// Assuming you have a service to get an email by ID
        Optional<PhoneNumber> phoneNumber = phoneNumberService.getPhoneNumberById(phoneId);
        if (phoneNumber.isPresent()) {
            model.addAttribute("phone", phoneNumber.get());
            model.addAttribute("personId", personId);
            return "person/modify/editable-phone-form :: phone-form";
        }
        model.addAttribute("errorMessage", "phone does not exist");
        return "person/modify/editable-phone-form :: phone-form";
    }

    @PostMapping("/person/{personId}/phone/save")
    public String savePhone(@ModelAttribute PhoneNumber phoneNumber, @PathVariable("personId") Long personId, Model model) {
        // Verify that 'email' here contains the ID correctly and not the email string.
        LOGGER.info("Saving phone for personId: {}, phoneId: {}", personId, phoneNumber.getPhoneId());
        Person person = personService.getPersonById(personId);
        phoneNumberService.saveOrUpdatePhone(person, phoneNumber);
        return "redirect:/person/view/" + personId;
    }


    @DeleteMapping("/person/{personId}/phone/delete/{phoneId}")
    public RedirectView deletePhone(@PathVariable("personId") Long personId, @PathVariable("phoneId") Long phoneId, RedirectAttributes redirectAttributes) {
        LOGGER.info("deleteEmail(...) - phoneId {}", phoneId);
        phoneNumberService.deletePhone(phoneId); // Assuming you have a service method for deleting the email
        redirectAttributes.addFlashAttribute("successMessage", "Phone Number Deleted!");
        RedirectView redirectView = new RedirectView("/person/view/" + personId);
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        LOGGER.info("completed call to delete phone");
        return redirectView;
    }


    //person address section
    @GetMapping("/person/{personId}/person-address/add")
    public String showAddPersonAddressForm(@PathVariable("personId") Long personId, Model model) {
        model.addAttribute("address", new PersonAddress());
        model.addAttribute("personId", personId);
        return "person/modify/editable-person-address-form :: address-form";
    }

    @GetMapping("/person/{personId}/person-address/edit/{addressId}")
    public String showEditPersonAddressForm(@PathVariable("personId") Long personId,
                                    @PathVariable("addressId") Long addressId, Model model) {
        // Optional<Email> emailById = emailService.getEmailById(phoneId);// Assuming you have a service to get an email by ID
        Optional<PersonAddress> personAddress = personAddressService.getPersonAddressById(addressId);
        if (personAddress.isPresent()) {
            model.addAttribute("address", personAddress.get());
            model.addAttribute("addressId", addressId);
            return "person/modify/editable-person-address-form :: address-form";
        }
        model.addAttribute("errorMessage", "phone does not exist");
        return "person/modify/editable-phone-form :: phone-form";
    }

    @PostMapping("/person/{personId}/person-address/save")
    public String savePersonAddress(@ModelAttribute PersonAddress personAddress, @PathVariable("personId") Long personId, Model model) {
        // Verify that 'personAddress' here contains the ID correctly and not the email string.
        LOGGER.info("Saving phone for personId: {}, addressId: {}", personId, personAddress.getAddressId());
        Person person = personService.getPersonById(personId);
        personAddressService.saveOrUpdatePersonAddress(person, personAddress);
        return "redirect:/person/view/" + personId;
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
    public String saveEmployer(@ModelAttribute Employer employer, @PathVariable("personId") Long personId, Model model) {
        // Verify that 'personAddress' here contains the ID correctly and not the email string.
        LOGGER.info("Saving phone for personId: {}, employerId: {}", personId, employer.getEmployerId());
        Person person = personService.getPersonById(personId);
        employerService.saveOrUpdateEmployer(person, employer);
        return "redirect:/person/view/" + personId;
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
