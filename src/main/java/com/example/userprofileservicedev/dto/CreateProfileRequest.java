package com.example.userprofileservicedev.dto;

import com.example.userprofileservicedev.constants.MessageKeys;
import com.example.userprofileservicedev.validation.AlphabeticName;
import com.example.userprofileservicedev.validation.ValidPhoneNumber;
import com.example.userprofileservicedev.validation.ValidPostalCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateProfileRequest {

    @NotBlank(message = "{" + MessageKeys.VALIDATION_EMAIL_REQUIRED + "}")
    @Email(message = "{" + MessageKeys.VALIDATION_EMAIL_INVALID + "}")
    private String email;

    @NotBlank(message = "{" + MessageKeys.VALIDATION_FIRSTNAME_REQUIRED + "}")
    @AlphabeticName(message = "{" + MessageKeys.VALIDATION_FIRSTNAME_INVALID + "}")
    private String firstName;

    @NotBlank(message = "{" + MessageKeys.VALIDATION_LASTNAME_REQUIRED + "}")
    @AlphabeticName(message = "{" + MessageKeys.VALIDATION_LASTNAME_INVALID + "}")
    private String lastName;

    @NotNull(message = "{" + MessageKeys.VALIDATION_BIRTHDATE_REQUIRED + "}")
    @Past(message = "{" + MessageKeys.VALIDATION_BIRTHDATE_PAST + "}")
    private LocalDate birthDate;

    @ValidPhoneNumber(message = "{" + MessageKeys.VALIDATION_PHONENUMBER_INVALID + "}")
    private String phoneNumber;

    private String street;
    private String city;
    private String country;

    @ValidPostalCode(message = "{" + MessageKeys.VALIDATION_POSTALCODE_INVALID + "}")
    private String postalCode;
}
