package com.example.userprofileservicedev.mapper;

import com.example.userprofileservicedev.domain.UserProfile;
import com.example.userprofileservicedev.dto.CreateProfileRequest;
import com.example.userprofileservicedev.dto.ProfileResponse;
import com.example.userprofileservicedev.dto.UpdateProfileRequest;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfile fromCreate(String userId, CreateProfileRequest req) {
        if (req == null) return null;
        return UserProfile.builder()
                .userId(userId)
                .email(req.getEmail())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .birthDate(req.getBirthDate())
                .phoneNumber(req.getPhoneNumber())
                .street(req.getStreet())
                .city(req.getCity())
                .country(req.getCountry())
                .postalCode(req.getPostalCode())
                .build();
    }

    public void applyPut(UserProfile entity, UpdateProfileRequest req) {
        if (entity == null || req == null) return;
        entity.setEmail(req.getEmail());
        entity.setFirstName(req.getFirstName());
        entity.setLastName(req.getLastName());
        entity.setBirthDate(req.getBirthDate());
        entity.setPhoneNumber(req.getPhoneNumber());
        entity.setStreet(req.getStreet());
        entity.setCity(req.getCity());
        entity.setCountry(req.getCountry());
        entity.setPostalCode(req.getPostalCode());
    }

    public ProfileResponse toResponse(UserProfile entity) {
        if (entity == null) return null;
        return ProfileResponse.builder()
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthDate(entity.getBirthDate())
                .phoneNumber(entity.getPhoneNumber())
                .street(entity.getStreet())
                .city(entity.getCity())
                .country(entity.getCountry())
                .postalCode(entity.getPostalCode())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
