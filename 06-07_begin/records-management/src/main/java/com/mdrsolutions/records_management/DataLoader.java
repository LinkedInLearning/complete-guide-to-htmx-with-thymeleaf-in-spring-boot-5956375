package com.mdrsolutions.records_management;

import com.github.javafaker.Faker;
import com.mdrsolutions.records_management.entity.*;
import com.mdrsolutions.records_management.repository.PersonRepository;
import com.mdrsolutions.records_management.repository.StudentRepository;
import com.mdrsolutions.records_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

@Component
public class DataLoader implements CommandLineRunner {

    //these variables are set this way for initializing dummy and "faker" data.
    private  Role adminRole;
    private  Role userRole;

    @Autowired
    private UserService userService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private Faker faker;

    @Override
    public void run(String... args) throws Exception {
        // Create roles
        adminRole = userService.createRole("ADMIN");
        userRole = userService.createRole("USER");

        // Create a person (guardian)
        Person person = new Person();
        person.setPrefix("Mr.");
        person.setFirstName("John");
        person.setMiddleName("Justin");
        person.setLastName("Jacobs Sr");
        person.setSuffix("III");
        person.setPersonType(PersonType.FATHER);
        person.setEmploymentStatus(EmploymentStatus.EMPLOYED);

        // Create emails
        Email personalEmail = new Email();
        personalEmail.setEmailAddress("john.jacobs.personal@mail.com");
        personalEmail.setType(EmailType.PERSONAL);
        personalEmail.setPerson(person);

        Email workEmail = new Email();
        workEmail.setEmailAddress("john.jacobs.work@mail.com");
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

        // Create and associate an employer if employed
        if (person.getEmploymentStatus().equals(EmploymentStatus.EMPLOYED)) {
            Employer employer = new Employer();
            employer.setBusinessName("TechCorp");
            employer.setAddress1("123 Tech Lane");
            employer.setCity("Innovation City");
            employer.setState("TechState");
            employer.setCountry("Techland");
            employer.setZip("12345");
            employer.setSupervisorName("Sarah Supervisor");
            employer.setSupervisorPhoneNumber("321-654-0987");
            employer.setSupervisorEmail("sarah.supervisor@techcorp.com");

            // Use the helper method to add the employer to the person
            person.addEmployer(employer);
        }

        // Create and add addresses to the person
        PersonAddress mailingAddress = new PersonAddress();
        mailingAddress.setAddressType(AddressType.MAILING);
        mailingAddress.setAddress1("789 Maple Street");
        mailingAddress.setAddress2("Apt 101");
        mailingAddress.setCity("Hometown");
        mailingAddress.setState("HomeState");
        mailingAddress.setZip("56789");
        mailingAddress.setCountryCd("US");
        mailingAddress.setPerson(person);  // Associate with person

        PersonAddress physicalAddress = new PersonAddress();
        physicalAddress.setAddressType(AddressType.PHYSICAL);
        physicalAddress.setAddress1("456 Oak Avenue");
        physicalAddress.setCity("Worktown");
        physicalAddress.setState("WorkState");
        physicalAddress.setZip("98765");
        physicalAddress.setCountryCd("US");
        physicalAddress.setPerson(person);  // Associate with person

        Set<PersonAddress> addresses = new HashSet<>();
        addresses.add(mailingAddress);
        addresses.add(physicalAddress);
        person.setAddresses(addresses);  // Associate addresses with person

        // Save the person (including the employers and addresses)
        personRepository.save(person);

        // Create a user with a hashed password and assign roles
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);
        User user = userService.createUser("jonjacobs95@mail.com", "securepassword", person, roles);

        // Create the first student
        Student student1 = new Student();
        student1.setFirstName("Jane");
        student1.setMiddleName("Doe");
        student1.setLastName("Jacobs");
        student1.setSuffix("");
        //student1.setSex(Sex.FEMALE);
        student1.setLastSchoolAttended("Old School");
        //student1.setCurrentGrade("5th");

        // Associate guardian
        Set<Person> guardians1 = new HashSet<>();
        guardians1.add(person);
        student1.setGuardians(guardians1);

        // Create complete prior school data for student1
        PriorSchool priorSchool1 = new PriorSchool();
        priorSchool1.setOrderIndex(1);
        priorSchool1.setSchoolName("Springfield Elementary");
        priorSchool1.setAddress("123 Main St");
        priorSchool1.setCity("Springfield");
        priorSchool1.setState("IL");
        priorSchool1.setZip("11111");
        priorSchool1.setPhoneNumber("555-1111");
        priorSchool1.setAdministratorFirstName("Seymour");
        priorSchool1.setAdministratorLastName("Skinner");
        priorSchool1.setGpa(new BigDecimal("3.5"));
        priorSchool1.setGradeLevel("5th Grade");
        priorSchool1.setDateStartedAttending(LocalDate.of(2022, 9, 1));
        priorSchool1.setDateLastAttended(LocalDate.of(2023, 6, 15));
        priorSchool1.setStudent(student1);

        PriorSchool priorSchool2 = new PriorSchool();
        priorSchool2.setOrderIndex(2);
        priorSchool2.setSchoolName("Springfield Junior High");
        priorSchool2.setAddress("124 Main St");
        priorSchool2.setCity("Springfield");
        priorSchool2.setState("MO");
        priorSchool2.setPhoneNumber("555-1111");
        priorSchool2.setAdministratorFirstName("Seymour");
        priorSchool2.setAdministratorLastName("Skinner");
        priorSchool2.setGpa(new BigDecimal("3.5"));
        priorSchool2.setGradeLevel("7th Grade");
        priorSchool2.setDateStartedAttending(LocalDate.of(2023, 9, 1));
        priorSchool2.setDateLastAttended(LocalDate.of(2024, 6, 15));
        priorSchool2.setStudent(student1);

        PriorSchool priorSchool3 = new PriorSchool();
        priorSchool3.setOrderIndex(3);
        priorSchool3.setSchoolName("Hartford High School");
        priorSchool3.setAddress("124 Back St");
        priorSchool3.setCity("Boliver");
        priorSchool3.setState("MO");
        priorSchool3.setPhoneNumber("123-555-1111");
        priorSchool3.setAdministratorFirstName("Seymour");
        priorSchool3.setAdministratorLastName("Brown");
        priorSchool3.setGpa(new BigDecimal("3.7"));
        priorSchool3.setGradeLevel("10th Grade");
        priorSchool3.setDateStartedAttending(LocalDate.of(2024, 9, 1));
        priorSchool3.setDateLastAttended(LocalDate.of(2025, 1, 15));
        priorSchool3.setStudent(student1);
        // Add prior school to student's prior schools set
        Set<PriorSchool> priorSchools1 = new HashSet<>();
        priorSchools1.add(priorSchool1);
        priorSchools1.add(priorSchool2);
        priorSchools1.add(priorSchool3);
        student1.setPriorSchools(priorSchools1);

        // Save the first student
        studentRepository.save(student1);

        // Create the second student
        Student student2 = new Student();
        student2.setFirstName("Jack");
        student2.setMiddleName("Thomas");
        student2.setLastName("Jacobs");
        student2.setSuffix("Jr.");
        student2.setSex(Sex.MALE);

        // Associate guardian
        Set<Person> guardians2 = new HashSet<>();
        guardians2.add(person);
        student2.setGuardians(guardians2);

        // Create partial prior school data for student2
        PriorSchool priorSchool4 = new PriorSchool();
        priorSchool4.setOrderIndex(1);
        priorSchool4.setSchoolName("Shelbyville Middle School");
        // Missing address, city, state, zip
        priorSchool4.setPhoneNumber("555-2222");
        // Missing administratorFirstName, administratorLastName
        priorSchool4.setGpa(new BigDecimal("2.8"));
        priorSchool4.setGradeLevel("6th Grade");
        // Missing dateStartedAttending
        priorSchool4.setDateLastAttended(LocalDate.of(2023, 5, 30));
        priorSchool4.setStudent(student2);

        // Add prior school to student's prior schools set
        Set<PriorSchool> priorSchools3 = new HashSet<>();
        priorSchools3.add(priorSchool4);
        student2.setPriorSchools(priorSchools3);

        // Save the second student
        studentRepository.save(student2);

        // Create the third student
        Student student3 = new Student();
        student3.setFirstName("Jill");
        student3.setMiddleName("Marie");
        student3.setLastName("Jacobs");
        student3.setSuffix("");
        student3.setSex(Sex.FEMALE);

        // Associate guardian
        Set<Person> guardians3 = new HashSet<>();
        guardians3.add(person);
        student3.setGuardians(guardians3);

        // Save the third student
        studentRepository.save(student3);

        //creating 200 fake persons --> added -06-05
        IntStream.range(0, 600)
                .forEach(i -> createFakePerson());

    }

    //Added 06-05
    //used by Admin Controller demonstration to generate fake data.
    public void createFakePerson(){
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = new User();
        user.setEmail(faker.internet().emailAddress());
        user.setEnabled(false);
        user.setRoles(roles);

        Person person = new Person();
        person.setPersonType(PersonType.FATHER);
        person.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        person.setFirstName(faker.name().firstName());
        person.setMiddleName(faker.name().nameWithMiddle());
        person.setLastName(faker.name().lastName());

        Set<Email> emails = new HashSet<>();
        Email email = new Email();
        email.setEmailAddress(faker.internet().emailAddress(person.getFirstName() + "." + person.getLastName()));
        email.setType(EmailType.PERSONAL);
        email.setPerson(person);
        emails.add(email);

        person.setEmails(emails);

        if (person.getEmploymentStatus().equals(EmploymentStatus.EMPLOYED)) {
            Employer employer = new Employer();
            employer.setBusinessName(faker.funnyName().name());
            employer.setAddress1(faker.address().streetAddress());
            employer.setCity(faker.address().city());
            employer.setState(faker.address().state());
            employer.setCountry(faker.address().countryCode());
            employer.setZip(faker.address().zipCode());
            employer.setSupervisorName(faker.name().name());
            employer.setSupervisorPhoneNumber(faker.phoneNumber().phoneNumber());
            employer.setSupervisorEmail(faker.internet().emailAddress());

            // Use the helper method to add the employer to the person
            person.addEmployer(employer);
        }

        // Create and add addresses to the person
        PersonAddress mailingAddress = new PersonAddress();
        mailingAddress.setAddressType(AddressType.MAILING);
        mailingAddress.setAddress1(faker.address().streetAddress());
        mailingAddress.setAddress2(faker.address().buildingNumber());
        mailingAddress.setCity(faker.address().city());
        mailingAddress.setState(faker.address().stateAbbr());
        mailingAddress.setZip(faker.address().zipCode());
        mailingAddress.setCountryCd(faker.address().countryCode());
        mailingAddress.setPerson(person);  // Associate with person

        Set<PersonAddress> addresses = new HashSet<>();
        addresses.add(mailingAddress);
        person.setAddresses(addresses);  // Associate addresses with person

        // Save the person (including the employers and addresses)
        personRepository.save(person);
        userService.createUser(faker.internet().emailAddress(), "securepassword", person, roles);

    }
}
