package com.example.userprofileservicedev.controller;

import com.example.userprofileservicedev.dto.UserProfileDTO;
import com.example.userprofileservicedev.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public UserProfileDTO getProfile(@AuthenticationPrincipal Jwt jwt) {
        return service.getProfile(jwt.getSubject());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileDTO createProfile(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UserProfileDTO dto) {
        return service.createProfile(jwt.getSubject(), dto);
    }

    @PutMapping
    public UserProfileDTO updateProfile(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UserProfileDTO dto) {
        return service.updateProfile(jwt.getSubject(), dto);
    }
}
