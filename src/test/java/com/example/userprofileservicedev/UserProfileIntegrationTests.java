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
        mockMvc.perform(get("/api/v1/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenCreateProfileWithValidToken_thenCreated() throws Exception {
        String token = obtainToken("user1");

        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("user1@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    void whenGetProfileAfterCreation_thenOk() throws Exception {
        String token = obtainToken("user2");
        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("user2@example.com")
                .birthDate(LocalDate.of(1992, 2, 2))
                .build();

        // Create
        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // Get
        mockMvc.perform(get("/api/v1/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user2@example.com"));
    }

    @Test
    void whenCreateProfileTwice_thenConflict() throws Exception {
        String token = obtainToken("user3");
        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@example.com")
                .birthDate(LocalDate.of(1985, 5, 5))
                .build();

        // First time
        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // Second time
        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void whenUpdateProfileWithFutureBirthDate_thenBadRequest() throws Exception {
        String token = obtainToken("user4");

        CreateProfileRequest createReq = CreateProfileRequest.builder()
                .firstName("Bob")
                .lastName("Brown")
                .email("bob@example.com")
                .birthDate(LocalDate.of(1980, 8, 8))
                .build();

        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        UpdateProfileRequest updateReq = UpdateProfileRequest.builder()
                .firstName("Bob")
                .lastName("Updated")
                .email("bob@example.com")
                .birthDate(LocalDate.now().plusDays(1))
                .build();

        mockMvc.perform(put("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation error"));
    }

    @Test
    void whenUpdateProfileWithValidData_thenOk() throws Exception {
        String token = obtainToken("user5");

        CreateProfileRequest createReq = CreateProfileRequest.builder()
                .firstName("Charlie")
                .lastName("Brown")
                .email("charlie@example.com")
                .birthDate(LocalDate.of(1980, 8, 8))
                .build();

        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        UpdateProfileRequest updateReq = UpdateProfileRequest.builder()
                .firstName("Charlie Updated")
                .lastName("Brown")
                .email("charlie.new@example.com")
                .birthDate(LocalDate.of(1980, 8, 8))
                .phoneNumber("123456789")
                .build();

        mockMvc.perform(put("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Charlie Updated"))
                .andExpect(jsonPath("$.email").value("charlie.new@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"));
    }

    @Test
    void whenUpdateNonExistentProfile_thenNotFound() throws Exception {
        String token = obtainToken("user6");

        UpdateProfileRequest updateReq = UpdateProfileRequest.builder()
                .firstName("Non")
                .lastName("Existent")
                .email("non@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        mockMvc.perform(put("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isNotFound());
    }

    private String obtainToken(String username) throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .build();

        String response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).path("access_token").asString();
    }
}
