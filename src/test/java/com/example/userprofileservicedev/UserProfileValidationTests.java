package com.example.userprofileservicedev;

import com.example.userprofileservicedev.dto.CreateProfileRequest;
import com.example.userprofileservicedev.dto.LoginRequest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.example.userprofileservicedev.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileValidationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenCreateWithInvalidEmail_thenBadRequest() throws Exception {
        String token = obtainToken();

        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_TEST)
                .lastName(LAST_NAME_USER)
                .email(EMAIL_INVALID)
                .birthDate(BIRTH_DATE_1990)
                .build();

        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JSON_PATH_TIMESTAMP).exists())
                .andExpect(jsonPath(JSON_PATH_STATUS).value(HTTP_STATUS_BAD_REQUEST))
                .andExpect(jsonPath(JSON_PATH_MESSAGE).value(MSG_VALIDATION_ERROR))
                .andExpect(jsonPath(JSON_PATH_PATH).value(API_V1_PROFILE))
                .andExpect(jsonPath(JSON_PATH_FIELD_ERRORS).isArray());
    }

    @Test
    void whenCreateWithFutureBirthDate_thenBadRequest() throws Exception {
        String token = obtainToken();

        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_TEST)
                .lastName(LAST_NAME_USER)
                .email(EMAIL_TEST)
                .birthDate(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JSON_PATH_TIMESTAMP).exists())
                .andExpect(jsonPath(JSON_PATH_STATUS).value(HTTP_STATUS_BAD_REQUEST))
                .andExpect(jsonPath(JSON_PATH_MESSAGE).value(MSG_VALIDATION_ERROR))
                .andExpect(jsonPath(JSON_PATH_PATH).value(API_V1_PROFILE))
                .andExpect(jsonPath(JSON_PATH_FIELD_ERRORS).isArray());
    }


    @Test
    void whenCreateWithAlphabeticPhoneNumber_thenBadRequest() throws Exception {
        String token = obtainToken();

        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_TEST)
                .lastName(LAST_NAME_USER)
                .email(EMAIL_PHONE_TEST)
                .birthDate(BIRTH_DATE_1990)
                .phoneNumber(PHONE_NUMBER_INVALID)
                .build();

        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JSON_PATH_TIMESTAMP).exists())
                .andExpect(jsonPath(JSON_PATH_STATUS).value(HTTP_STATUS_BAD_REQUEST))
                .andExpect(jsonPath(JSON_PATH_MESSAGE).value(MSG_VALIDATION_ERROR))
                .andExpect(jsonPath(JSON_PATH_FIELD_ERRORS).isArray());
    }

    @Test
    void whenCreateWithInvalidPostalCode_thenBadRequest() throws Exception {
        String token = obtainToken();

        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_TEST)
                .lastName(LAST_NAME_USER)
                .email(EMAIL_POSTAL_TEST)
                .birthDate(BIRTH_DATE_1990)
                .postalCode(POSTAL_CODE_INVALID)
                .build();

        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JSON_PATH_TIMESTAMP).exists())
                .andExpect(jsonPath(JSON_PATH_STATUS).value(HTTP_STATUS_BAD_REQUEST))
                .andExpect(jsonPath(JSON_PATH_MESSAGE).value(MSG_VALIDATION_ERROR))
                .andExpect(jsonPath(JSON_PATH_FIELD_ERRORS).isArray());
    }


    private String obtainToken() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username(USERNAME_VALIDATOR)
                .build();

        String response = mockMvc.perform(post(API_V1_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).path(ACCESS_TOKEN_FIELD).asString();
    }
}
