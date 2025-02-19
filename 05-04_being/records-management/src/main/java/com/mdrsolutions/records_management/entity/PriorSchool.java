package com.mdrsolutions.records_management.entity;

import com.mdrsolutions.records_management.annotation.MarkForReview;
import com.mdrsolutions.records_management.annotation.RequirementLevel;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class PriorSchool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MarkForReview(
            name = "School Name",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String schoolName;
    @MarkForReview(
            name = "School Address",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String address;
    @MarkForReview(
            name = "School City",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String city;

    @MarkForReview(
            name = "School State/Province",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String state;
    @MarkForReview(
            name = "School Zip Code",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String zip;
    @MarkForReview(
            name = "School Phone Number",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String phoneNumber;
    @MarkForReview(
            name = "School - Administrator First Name",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String administratorFirstName;
    @MarkForReview(
            name = "School - Administrator Last Name",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String administratorLastName;
    @MarkForReview(
            name = "School - GPA",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private BigDecimal gpa;
    @MarkForReview(
            name = "School - Grade Level",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String gradeLevel;

    @MarkForReview(
            name = "School - Date Started Attending",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private LocalDate dateStartedAttending;

    @MarkForReview(
            name = "School - Date Last Attending",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private LocalDate dateLastAttended;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    // Constructors

    public PriorSchool() {
    }

    public PriorSchool(String schoolName, String address, String city, String state, String zip,
                       String phoneNumber, String administratorFirstName, String administratorLastName,
                       BigDecimal gpa, String gradeLevel, LocalDate dateStartedAttending,
                       LocalDate dateLastAttended, Student student) {
        this.schoolName = schoolName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.administratorFirstName = administratorFirstName;
        this.administratorLastName = administratorLastName;
        this.gpa = gpa;
        this.gradeLevel = gradeLevel;
        this.dateStartedAttending = dateStartedAttending;
        this.dateLastAttended = dateLastAttended;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAdministratorFirstName() {
        return administratorFirstName;
    }

    public void setAdministratorFirstName(String administratorFirstName) {
        this.administratorFirstName = administratorFirstName;
    }

    public String getAdministratorLastName() {
        return administratorLastName;
    }

    public void setAdministratorLastName(String administratorLastName) {
        this.administratorLastName = administratorLastName;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public LocalDate getDateStartedAttending() {
        return dateStartedAttending;
    }

    public void setDateStartedAttending(LocalDate dateStartedAttending) {
        this.dateStartedAttending = dateStartedAttending;
    }

    public LocalDate getDateLastAttended() {
        return dateLastAttended;
    }

    public void setDateLastAttended(LocalDate dateLastAttended) {
        this.dateLastAttended = dateLastAttended;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
