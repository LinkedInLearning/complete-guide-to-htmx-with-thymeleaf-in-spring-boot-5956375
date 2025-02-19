package com.mdrsolutions.records_management.entity;

import com.mdrsolutions.records_management.annotation.MarkForReview;
import com.mdrsolutions.records_management.annotation.RequirementLevel;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    @MarkForReview(
            name = "Employment Status",
            description = "Indicates if the person is employed, unemployed or self employed",
            level = RequirementLevel.REQUIRED,
            relatedField = "employers", // This points to the related Employer entity.
            conditionField = "employmentStatus" // This indicates that the related entity should be checked only if employment status is unemployed.
    )
    private EmploymentStatus employmentStatus;


    @MarkForReview(
            name = "Prefix",
            description = "Title before the person's name (e.g., Mr., Ms., Dr.)",
            level = RequirementLevel.SUGGESTED
    )
    private String prefix;

    @MarkForReview(
            name = "First Name",
            description = "The first name of the person",
            level = RequirementLevel.REQUIRED
    )
    private String firstName;

    @MarkForReview(
            name = "Middle Name",
            description = "The middle name of the person, if available",
            level = RequirementLevel.SUGGESTED
    )
    private String middleName;

    @MarkForReview(
            name = "Last Name",
            description = "The last name of the person",
            level = RequirementLevel.REQUIRED
    )
    private String lastName;

    private String suffix;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MarkForReview(
            name = "Email Addresses",
            description = "Set of email addresses associated with the person",
            level = RequirementLevel.REQUIRED
    )
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Email> emails;

    @MarkForReview(
            name = "Phone Numbers",
            description = "Set of phone numbers associated with the person",
            level = RequirementLevel.REQUIRED
    )
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhoneNumber> phoneNumbers;

    @MarkForReview(
            name = "Person Type",
            description = "Type of person (e.g., STUDENT, GUARDIAN)",
            level = RequirementLevel.REQUIRED
    )
    @Enumerated(EnumType.STRING)
    private PersonType personType;

    @MarkForReview(
            name = "Person - Legal Guardian Type",
            description = "Type of legal guardian if the person is a guardian",
            level = RequirementLevel.REQUIRED
    )
    @Enumerated(EnumType.STRING)
    private LegalGuardianType legalGuardianType;

    @ManyToMany(mappedBy = "guardians")
    private Set<Student> students;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Employer> employers = new HashSet<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonAddress> addresses = new HashSet<>();

    public Set<PersonAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<PersonAddress> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(PersonAddress address) {
        addresses.add(address);
        address.setPerson(this);
    }

    public void removeAddress(PersonAddress address) {
        addresses.remove(address);
        address.setPerson(null);
    }

    // Helper method to add an employer
    public void addEmployer(Employer employer) {
        employers.add(employer);
        employer.setPerson(this);
    }

    // Helper method to remove an employer
    public void removeEmployer(Employer employer) {
        employers.remove(employer);
        employer.setPerson(null);
    }

    // Getters and Setters
    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Email> getEmails() {
        return emails;
    }

    public void setEmails(Set<Email> emails) {
        this.emails = emails;
    }

    public Set<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    public LegalGuardianType getLegalGuardianType() {
        return legalGuardianType;
    }

    public void setLegalGuardianType(LegalGuardianType legalGuardianType) {
        this.legalGuardianType = legalGuardianType;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Employer> getEmployers() {
        return employers;
    }

    public void setEmployers(Set<Employer> employers) {
        this.employers = employers;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }
}
