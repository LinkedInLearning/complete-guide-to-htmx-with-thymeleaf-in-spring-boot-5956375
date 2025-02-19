package com.mdrsolutions.records_management.entity;

import com.mdrsolutions.records_management.annotation.MarkForReview;
import com.mdrsolutions.records_management.annotation.RequirementLevel;
import jakarta.persistence.*;

@Entity
public class PersonAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @MarkForReview(
            name = "Person Address Type",
            description = "Type of address (must be either MAILING, PHYSICAL)",
            level = RequirementLevel.REQUIRED
    )
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @MarkForReview(
            name = "Address Line 1",
            description = "Primary address line",
            level = RequirementLevel.REQUIRED
    )
    private String address1;

    private String address2;

    @MarkForReview(
            name = "City",
            description = "City for the address",
            level = RequirementLevel.REQUIRED
    )
    private String city;

    @MarkForReview(
            name = "State",
            description = "State for the address",
            level = RequirementLevel.REQUIRED
    )
    private String state;

    @MarkForReview(
            name = "ZIP",
            description = "ZIP code for the address",
            level = RequirementLevel.REQUIRED
    )
    private String zip;

    @MarkForReview(
            name = "Country Code",
            description = "Country code for the address",
            level = RequirementLevel.REQUIRED
    )
    private String countryCd;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // Getters and Setters
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountryCd() {
        return countryCd;
    }

    public void setCountryCd(String countryCd) {
        this.countryCd = countryCd;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
