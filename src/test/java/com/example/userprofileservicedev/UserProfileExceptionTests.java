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
class UserProfileExceptionTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenProfileNotFound_then404() throws Exception {
        String token = obtainToken(USERNAME_NOT_FOUND);

        mockMvc.perform(get(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JSON_PATH_STATUS).value(HTTP_STATUS_NOT_FOUND))
                .andExpect(jsonPath(JSON_PATH_MESSAGE).value(MSG_PROFILE_NOT_FOUND))
                .andExpect(jsonPath(JSON_PATH_PATH).value(API_V1_PROFILE));
    }

    @Test
    void whenProfileConflict_then409() throws Exception {
        String token = obtainToken(USERNAME_CONFLICT);

        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName(FIRST_NAME_CONFLICT)
                .lastName(LAST_NAME_USER)
                .email(EMAIL_CONFLICT)
                .birthDate(BIRTH_DATE_1990)
                .build();

        // First creation
        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // Duplicate creation
        mockMvc.perform(post(API_V1_PROFILE)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath(JSON_PATH_STATUS).value(HTTP_STATUS_CONFLICT))
                .andExpect(jsonPath(JSON_PATH_MESSAGE).value(MSG_PROFILE_CONFLICT))
                .andExpect(jsonPath(JSON_PATH_PATH).value(API_V1_PROFILE));
    }

    private String obtainToken(String username) throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .build();

        String response = mockMvc.perform(post(API_V1_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).path(ACCESS_TOKEN_FIELD).asString();
    }
}
