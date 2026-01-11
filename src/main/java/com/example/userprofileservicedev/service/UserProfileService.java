package com.example.userprofileservicedev.service;

import com.example.userprofileservicedev.domain.UserProfile;
import com.example.userprofileservicedev.dto.CreateProfileRequest;
import com.example.userprofileservicedev.dto.ProfileResponse;
import com.example.userprofileservicedev.dto.UpdateProfileRequest;
import com.example.userprofileservicedev.exception.ConflictException;
import com.example.userprofileservicedev.exception.NotFoundException;
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

    public ProfileResponse getMyProfile(String userId) {
        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado"));
        return mapper.toResponse(profile);
    }

    public ProfileResponse createMyProfile(String userId, CreateProfileRequest req) {
        if (repository.existsByUserId(userId)) {
            throw new ConflictException("El perfil ya existe para este usuario");
        }
        UserProfile profile = mapper.fromCreate(userId, req);
        return mapper.toResponse(repository.save(profile));
    }

    public ProfileResponse putMyProfile(String userId, UpdateProfileRequest req) {
        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Perfil no encontrado"));

        mapper.applyPut(profile, req);

        return mapper.toResponse(repository.save(profile));
    }
}
