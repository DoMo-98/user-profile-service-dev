package com.example.userprofileservicedev.controller;

import com.example.userprofileservicedev.dto.CreateProfileRequest;
import com.example.userprofileservicedev.dto.ProfileResponse;
import com.example.userprofileservicedev.dto.UpdateProfileRequest;
import com.example.userprofileservicedev.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal Jwt jwt) {
        // Obtenemos el userId directamente de la claim "sub" del token JWT inyectado
        String userId = jwt.getSubject();
        return ResponseEntity.ok(service.getMyProfile(userId));
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateProfileRequest req) {
        // Obtenemos el userId directamente de la claim "sub" del token JWT inyectado
        String userId = jwt.getSubject();
        ProfileResponse response = service.createMyProfile(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProfileRequest req) {
        // Obtenemos el userId directamente de la claim "sub" del token JWT inyectado
        String userId = jwt.getSubject();
        return ResponseEntity.ok(service.putMyProfile(userId, req));
    }
}
