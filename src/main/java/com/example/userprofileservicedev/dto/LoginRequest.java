package com.example.userprofileservicedev.dto;

import com.example.userprofileservicedev.constants.MessageKeys;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "{" + MessageKeys.VALIDATION_USERNAME_REQUIRED + "}")
    private String username;
    private String password;
}
