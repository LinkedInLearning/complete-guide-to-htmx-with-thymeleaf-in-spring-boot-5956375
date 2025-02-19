package com.mdrsolutions.records_management.controller.dto;

import com.mdrsolutions.records_management.entity.AddressType;

public record PersonAddressDto(
        Long addressId,
        AddressType addressType,
        String address1,
        String address2,
        String city,
        String state,
        String zip,
        String countryCd,
        Long personId
) {
    public static PersonAddressDto fromEntity(com.mdrsolutions.records_management.entity.PersonAddress entity) {
        return new PersonAddressDto(
                entity.getAddressId(),
                entity.getAddressType(),
                entity.getAddress1(),
                entity.getAddress2(),
                entity.getCity(),
                entity.getState(),
                entity.getZip(),
                entity.getCountryCd(),
                entity.getPerson() != null ? entity.getPerson().getPersonId() : null
        );
    }
}
