package com.example.userprofileservicedev.service;

import com.example.userprofileservicedev.domain.UserProfile;
import com.example.userprofileservicedev.dto.CreateProfileRequest;
import com.example.userprofileservicedev.dto.ProfileResponse;
import com.example.userprofileservicedev.dto.UpdateProfileRequest;
import com.example.userprofileservicedev.exception.ResourceAlreadyExistsException;
import com.example.userprofileservicedev.exception.ResourceNotFoundException;
import com.example.userprofileservicedev.mapper.UserProfileMapper;
import com.example.userprofileservicedev.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository repository;
    private final UserProfileMapper mapper;

    public UserProfileService(UserProfileRepository repository, UserProfileMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProfileResponse getProfile(String userId) {
        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));
        return mapper.toResponse(profile);
    }

    public ProfileResponse createProfile(String userId, CreateProfileRequest req) {
        if (repository.existsByUserId(userId)) {
            throw new ResourceAlreadyExistsException("El perfil ya existe para este usuario");
        }
        UserProfile profile = mapper.fromCreate(userId, req);
        return mapper.toResponse(repository.save(profile));
    }

    public ProfileResponse updateProfile(String userId, UpdateProfileRequest req) {
        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));

        mapper.applyPut(profile, req);

        return mapper.toResponse(repository.save(profile));
    }
}
