package com.mdrsolutions.records_management.entity;

import com.mdrsolutions.records_management.annotation.MarkForReview;
import com.mdrsolutions.records_management.annotation.RequirementLevel;
import jakarta.persistence.*;

@Entity
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employerId;

    @MarkForReview(
            name = "Employer Business Name",
            description = "The name of the business or company",
            level = RequirementLevel.REQUIRED
    )
    private String businessName;

    @MarkForReview(
            name = "Employer Address 1",
            description = "Primary address of the business",
            level = RequirementLevel.REQUIRED
    )
    private String address1;

    @MarkForReview(
            name = "Employer Address 2",
            description = "Secondary address of the business, if applicable",
            level = RequirementLevel.SUGGESTED
    )
    private String address2;

    @MarkForReview(
            name = "Employer City",
            description = "City where the business is located",
            level = RequirementLevel.REQUIRED
    )
    private String city;

    @MarkForReview(
            name = "Employer State",
            description = "State where the business is located",
            level = RequirementLevel.REQUIRED
    )
    private String state;

    @MarkForReview(
            name = "Employer Country",
            description = "Country where the business is located",
            level = RequirementLevel.REQUIRED
    )
    private String country;

    @MarkForReview(
            name = "Employer Zip",
            description = "Postal code of the business location",
            level = RequirementLevel.REQUIRED
    )
    private String zip;

    @MarkForReview(
            name = "Employer Direct Supervisor's Full Name",
            description = "Full name of the direct supervisor at the business",
            level = RequirementLevel.REQUIRED
    )
    private String supervisorName;

    @MarkForReview(
            name = "Employer Supervisor Phone Number",
            description = "Phone number of the direct supervisor",
            level = RequirementLevel.REQUIRED
    )
    private String supervisorPhoneNumber;

    @MarkForReview(
            name = "SEmployer upervisor Email",
            description = "Email address of the direct supervisor",
            level = RequirementLevel.REQUIRED

    )
    private String supervisorEmail;

    @MarkForReview(
            name = "Employer Type",
            description = "The type of employment (Fulltime, Partime, Other). )",
            level = RequirementLevel.REQUIRED
    )
    @Enumerated(EnumType.STRING)
    private EmployerType type;

    @MarkForReview(
            name = "Employment Position",
            description = "Position held in organization. This could cashier, teacher, owner, manager, waitor, cook, etc... )",
            level = RequirementLevel.REQUIRED
    )
    private String position;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // Getters and Setters
    public Long getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorPhoneNumber() {
        return supervisorPhoneNumber;
    }

    public void setSupervisorPhoneNumber(String supervisorPhoneNumber) {
        this.supervisorPhoneNumber = supervisorPhoneNumber;
    }

    public String getSupervisorEmail() {
        return supervisorEmail;
    }

    public void setSupervisorEmail(String supervisorEmail) {
        this.supervisorEmail = supervisorEmail;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public EmployerType getType() {
        return type;
    }

    public void setType(EmployerType type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
