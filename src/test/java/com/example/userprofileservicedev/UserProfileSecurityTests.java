package com.example.userprofileservicedev;

import com.example.userprofileservicedev.dto.CreateProfileRequest;
import com.example.userprofileservicedev.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.userprofileservicedev.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenLogin_thenReceiveToken() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username(USERNAME_TEST)
                .build();

        mockMvc.perform(post(API_V1_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_ACCESS_TOKEN).exists())
                .andExpect(jsonPath(JSON_PATH_TOKEN_TYPE).value(TOKEN_TYPE_BEARER))
                .andExpect(jsonPath(JSON_PATH_EXPIRES_IN).value(TOKEN_EXPIRES_IN_SECONDS));
    }

    @Test
    void whenGetProfileWithoutToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get(API_V1_PROFILE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenAccessWithToken_thenSuccess() throws Exception {
        // Login to get token
        LoginRequest loginRequest = LoginRequest.builder()
                .username(USERNAME_TEST)
                .build();

        String response = mockMvc.perform(post(API_V1_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(response).path(ACCESS_TOKEN_FIELD).asString();

        // Create profile
        CreateProfileRequest dto = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_TEST)
                .lastName(LAST_NAME_USER)
                .email(EMAIL_TEST)
                .birthDate(BIRTH_DATE_1990)
                .build();

        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Get profile
        mockMvc.perform(get(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_NAME_TEST))
                .andExpect(jsonPath(JSON_PATH_LAST_NAME).value(LAST_NAME_USER));
    }
}
