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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .firstName("Test")
                .lastName("User")
                .email("invalid-email")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateWithFutureBirthDate_thenBadRequest() throws Exception {
        String token = obtainToken();

        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .birthDate(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    private String obtainToken() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("validator")
                .build();

        String response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("access_token").asText();
    }
}
