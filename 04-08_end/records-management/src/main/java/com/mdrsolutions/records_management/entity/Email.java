package com.mdrsolutions.records_management.entity;


import com.mdrsolutions.records_management.annotation.MarkForReview;
import com.mdrsolutions.records_management.annotation.RequirementLevel;
import jakarta.persistence.*;

@Entity
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailId;
    @MarkForReview(
            name = "Email Address",
            description = "Email address of the person",
            level = RequirementLevel.REQUIRED
    )
    private String emailAddress;
    @MarkForReview(
            name = "Email Address Type",
            description = "The type of Email address of the person (home, work, school, etc...)",
            level = RequirementLevel.REQUIRED
    )
    @Enumerated(EnumType.STRING)
    private EmailType type;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    // Getters and Setters
    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public EmailType getType() {
        return type;
    }

    public void setType(EmailType type) {
        this.type = type;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

