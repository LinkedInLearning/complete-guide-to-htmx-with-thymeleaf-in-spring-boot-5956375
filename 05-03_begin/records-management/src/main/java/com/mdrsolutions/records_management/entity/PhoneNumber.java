package com.mdrsolutions.records_management.entity;

import jakarta.persistence.*;


@Entity
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phoneId;
    private String number;

    @Enumerated(EnumType.STRING)
    private PhoneType type;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    // Getters and Setters
    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PhoneType getType() {
        return type;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

