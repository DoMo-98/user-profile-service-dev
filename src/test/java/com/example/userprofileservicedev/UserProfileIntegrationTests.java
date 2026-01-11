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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        // 1) GET /api/v1/profile sin Authorization -> 401
        mockMvc.perform(get("/api/v1/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenCreateProfileWithValidToken_thenCreated() throws Exception {
        // 2) POST /api/v1/profile con token válido -> 201
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
        // 3) GET /api/v1/profile con token válido tras crear -> 200
        String token = obtainToken("user2");
        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("user2@example.com")
                .birthDate(LocalDate.of(1992, 2, 2))
                .build();

        // Crear
        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // Obtener
        mockMvc.perform(get("/api/v1/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user2@example.com"));
    }

    @Test
    void whenCreateProfileTwice_thenConflict() throws Exception {
        // 4) POST /api/v1/profile dos veces con el mismo token -> 409
        String token = obtainToken("user3");
        CreateProfileRequest req = CreateProfileRequest.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@example.com")
                .birthDate(LocalDate.of(1985, 5, 5))
                .build();

        // Primera vez
        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // Segunda vez
        mockMvc.perform(post("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void whenUpdateProfileWithFutureBirthDate_thenBadRequest() throws Exception {
        // 5) PUT /api/v1/profile con birthDate futura -> 400
        String token = obtainToken("user4");
        
        // Primero creamos un perfil válido para poder actualizarlo
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

        // Intentamos actualizar con fecha futura
        UpdateProfileRequest updateReq = UpdateProfileRequest.builder()
                .firstName("Bob")
                .lastName("Updated")
                .email("bob@example.com")
                .birthDate(LocalDate.now().plusDays(1)) // FUTURO
                .build();

        mockMvc.perform(put("/api/v1/profile")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Error de validación"));
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
