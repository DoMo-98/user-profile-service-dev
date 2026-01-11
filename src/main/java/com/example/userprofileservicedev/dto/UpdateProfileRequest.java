package com.example.userprofileservicedev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    @NotBlank(message = "{validation.email.required}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.firstName.required}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.required}")
    private String lastName;

    @Past(message = "{validation.birthDate.past}")
    private LocalDate birthDate;

    private String phoneNumber;
    private String street;
    private String city;
    private String country;
    private String postalCode;
}
