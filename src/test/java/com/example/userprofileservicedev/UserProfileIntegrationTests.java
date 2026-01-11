package com.example.userprofileservicedev;

import com.example.userprofileservicedev.dto.CreateProfileRequest;
import com.example.userprofileservicedev.dto.LoginRequest;
import com.example.userprofileservicedev.dto.UpdateProfileRequest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.example.userprofileservicedev.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserProfileIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetProfileWithoutToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get(API_V1_PROFILE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenCreateProfileWithValidToken_thenCreated() throws Exception {
        String token = obtainToken(USERNAME_USER1);

        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_JOHN)
                .lastName(LAST_NAME_DOE)
                .email(EMAIL_USER1)
                .birthDate(BIRTH_DATE_1990)
                .build();

        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(JSON_PATH_EMAIL).value(EMAIL_USER1));
    }

    @Test
    void whenGetProfileAfterCreation_thenOk() throws Exception {
        String token = obtainToken(USERNAME_USER2);
        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_JANE)
                .lastName(LAST_NAME_DOE)
                .email(EMAIL_USER2)
                .birthDate(BIRTH_DATE_1992)
                .build();

        // Create
        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // Get
        mockMvc.perform(get(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_EMAIL).value(EMAIL_USER2));
    }

    @Test
    void whenCreateProfileTwice_thenConflict() throws Exception {
        String token = obtainToken(USERNAME_USER3);
        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_ALICE)
                .lastName(LAST_NAME_SMITH)
                .email(EMAIL_ALICE)
                .birthDate(BIRTH_DATE_1985)
                .build();

        // First time
        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // Second time
        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void whenUpdateProfileWithFutureBirthDate_thenBadRequest() throws Exception {
        String token = obtainToken(USERNAME_USER4);

        CreateProfileRequest createReq = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_BOB)
                .lastName(LAST_NAME_BROWN)
                .email(EMAIL_BOB)
                .birthDate(BIRTH_DATE_1980)
                .build();

        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        UpdateProfileRequest updateReq = UpdateProfileRequest.builder()
                .firstName(FIRST_NAME_BOB)
                .lastName(LAST_NAME_UPDATED)
                .email(EMAIL_BOB)
                .birthDate(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(put(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JSON_PATH_STATUS).value(HTTP_STATUS_BAD_REQUEST))
                .andExpect(jsonPath(JSON_PATH_MESSAGE).value(MSG_VALIDATION_ERROR));
    }

    @Test
    void whenUpdateProfileWithValidData_thenOk() throws Exception {
        String token = obtainToken(USERNAME_USER5);

        CreateProfileRequest createReq = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_CHARLIE)
                .lastName(LAST_NAME_BROWN)
                .email(EMAIL_CHARLIE)
                .birthDate(BIRTH_DATE_1980)
                .build();

        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        UpdateProfileRequest updateReq = UpdateProfileRequest.builder()
                .firstName(FIRST_NAME_CHARLIE_UPDATED)
                .lastName(LAST_NAME_BROWN)
                .email(EMAIL_CHARLIE_NEW)
                .birthDate(BIRTH_DATE_1980)
                .phoneNumber(PHONE_NUMBER_DEFAULT)
                .build();

        mockMvc.perform(put(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_NAME_CHARLIE_UPDATED))
                .andExpect(jsonPath(JSON_PATH_EMAIL).value(EMAIL_CHARLIE_NEW))
                .andExpect(jsonPath(JSON_PATH_PHONE_NUMBER).value(PHONE_NUMBER_DEFAULT));
    }

    @Test
    void whenUpdateNonExistentProfile_thenNotFound() throws Exception {
        String token = obtainToken(USERNAME_USER6);

        UpdateProfileRequest updateReq = UpdateProfileRequest.builder()
                .firstName(FIRST_NAME_NON)
                .lastName(LAST_NAME_EXISTENT)
                .email(EMAIL_NON)
                .birthDate(BIRTH_DATE_1990)
                .build();

        mockMvc.perform(put(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isNotFound());
    }

    private String obtainToken(String username) throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .build();

        String response = mockMvc.perform(post(API_V1_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).path(ACCESS_TOKEN_FIELD).asString();
    }
}
