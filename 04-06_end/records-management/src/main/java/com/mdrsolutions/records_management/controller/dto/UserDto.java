package com.mdrsolutions.records_management.controller.dto;
import com.mdrsolutions.records_management.validation.ValidPersonType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserDto(
        @NotBlank(message = "First name is required") String firstName,
        String middleName,
        @NotBlank(message = "Last name is required") String lastName,
        @Email(message = "Email should be valid")String email,
        @Size(min = 8, message = "Password must be at least 8 characters long")String password,
        @ValidPersonType(message = "Person Type is required.  You cannot select the default value!") String personType,
        @AssertTrue(message = "You must accept the terms and conditions") boolean checkedAgreement) {
}
