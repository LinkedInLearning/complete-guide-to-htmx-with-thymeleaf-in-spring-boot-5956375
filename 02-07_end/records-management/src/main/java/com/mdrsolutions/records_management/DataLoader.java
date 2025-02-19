package com.mdrsolutions.records_management;

import com.mdrsolutions.records_management.entity.*;
import com.mdrsolutions.records_management.repository.PersonRepository;
import com.mdrsolutions.records_management.repository.StudentRepository;
import com.mdrsolutions.records_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create roles
        Role adminRole = userService.createRole("ADMIN");
        Role userRole = userService.createRole("USER");

        // Create a person
        Person person = new Person();
        person.setPrefix("Mr.");
        person.setFirstName("John");
        person.setMiddleName("Justin");
        person.setLastName("Jacobs Sr");
        person.setSuffix("III");
        person.setPersonType(PersonType.FATHER);

        // Create emails
        Email personalEmail = new Email();
        personalEmail.setEmail("john.jacobs.personal@mail.com");
        personalEmail.setType(EmailType.PERSONAL);
        personalEmail.setPerson(person);

        Email workEmail = new Email();
        workEmail.setEmail("john.jacobs.work@mail.com");
        workEmail.setType(EmailType.WORK);
        workEmail.setPerson(person);

        Set<Email> emails = new HashSet<>();
        emails.add(personalEmail);
        emails.add(workEmail);
        person.setEmails(emails);

        // Create phone numbers
        PhoneNumber homePhone = new PhoneNumber();
        homePhone.setNumber("123-456-7890");
        homePhone.setType(PhoneType.HOME);
        homePhone.setPerson(person);

        PhoneNumber mobilePhone = new PhoneNumber();
        mobilePhone.setNumber("098-765-4321");
        mobilePhone.setType(PhoneType.MOBILE);
        mobilePhone.setPerson(person);

        Set<PhoneNumber> phoneNumbers = new HashSet<>();
        phoneNumbers.add(homePhone);
        phoneNumbers.add(mobilePhone);
        person.setPhoneNumbers(phoneNumbers);

        // Save the person
        personRepository.save(person);

        // Create a user with a hashed password and assign roles
        Set<Role> roles = new HashSet<>();
//        roles.add(adminRole);
        roles.add(userRole);
        User user = userService.createUser("jonjacobs95@mail.com", "securepassword", person, roles);

        // Create a student
        Student student = new Student();
        student.setFirstName("Jane");
        student.setMiddleName("Doe");
        student.setLastName("Jacobs");
        student.setSuffix("");
        student.setSex(Sex.FEMALE);
        student.setLastSchoolAttended("Old School");
        student.setCurrentGrade("5th");

        Set<Person> guardians = new HashSet<>();
        guardians.add(person);
        student.setGuardians(guardians);

        // Save the student
        studentRepository.save(student);
    }
}
