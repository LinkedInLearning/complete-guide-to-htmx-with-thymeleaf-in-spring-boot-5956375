package com.mdrsolutions.records_management.entity;

import com.mdrsolutions.records_management.annotation.MarkForReview;
import com.mdrsolutions.records_management.annotation.RequirementLevel;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    @MarkForReview(
            name = "Student First Name",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String firstName;
    @MarkForReview(
            name = "Student Middle Name",
            description = "Is required. ",
            level = RequirementLevel.SUGGESTED
    )
    private String middleName;
    @MarkForReview(
            name = "Student Last Name",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private String lastName;
    private String suffix;

    @Enumerated(EnumType.STRING)
    @MarkForReview(
            name = "Student Sex",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private Sex sex;

    @MarkForReview(
            name = "Student Date of Birth",
            description = "Is required. ",
            level = RequirementLevel.REQUIRED
    )
    private LocalDate dateOfBirth;

    @MarkForReview(
            name = "Name of School",
            description = "Last School Attended is Required. ",
            level = RequirementLevel.REQUIRED
    )
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PriorSchool> priorSchools = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "student_guardians",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> guardians;

    @Transient
    private String lastSchoolAttended;

    public String getLastSchoolAttended() {
        return lastSchoolAttended;
    }

    public void setLastSchoolAttended(String lastSchoolAttended) {
        this.lastSchoolAttended = lastSchoolAttended;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<PriorSchool> getPriorSchools() {
        return priorSchools;
    }

    public void setPriorSchools(Set<PriorSchool> priorSchools) {
        this.priorSchools = priorSchools;
    }

    // Add helper methods to manage the relationship

    public void addPriorSchool(PriorSchool priorSchool) {
        priorSchools.add(priorSchool);
        priorSchool.setStudent(this);
    }

    public void removePriorSchool(PriorSchool priorSchool) {
        priorSchools.remove(priorSchool);
        priorSchool.setStudent(null);
    }

    public Set<Person> getGuardians() {
        return guardians;
    }

    public void setGuardians(Set<Person> guardians) {
        this.guardians = guardians;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", suffix='" + suffix + '\'' +
                ", sex=" + sex +
                ", dateOfBirth=" + dateOfBirth +
                ", priorSchools=" + priorSchools +
                ", guardians=" + guardians +
                ", lastSchoolAttended='" + lastSchoolAttended + '\'' +
                '}';
    }
}
