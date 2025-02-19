package com.mdrsolutions.records_management.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;

    @Enumerated(EnumType.STRING)
    private Sex sex;
    private String lastSchoolAttended;
    private String currentGrade;

    @ManyToMany
    @JoinTable(
            name = "student_guardians",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> guardians;

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

    public String getLastSchoolAttended() {
        return lastSchoolAttended;
    }

    public void setLastSchoolAttended(String lastSchoolAttended) {
        this.lastSchoolAttended = lastSchoolAttended;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }

    public Set<Person> getGuardians() {
        return guardians;
    }

    public void setGuardians(Set<Person> guardians) {
        this.guardians = guardians;
    }
}
