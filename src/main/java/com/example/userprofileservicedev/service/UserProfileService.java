package com.example.userprofileservicedev.service;

import com.example.userprofileservicedev.domain.UserProfile;
import com.example.userprofileservicedev.dto.UserProfileDTO;
import com.example.userprofileservicedev.exception.ResourceAlreadyExistsException;
import com.example.userprofileservicedev.exception.ResourceNotFoundException;
import com.example.userprofileservicedev.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public UserProfileDTO getProfile(String userId) {
        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));
        return mapToDTO(profile);
    }

    public UserProfileDTO createProfile(String userId, UserProfileDTO dto) {
        if (repository.findByUserId(userId).isPresent()) {
            throw new ResourceAlreadyExistsException("El perfil ya existe para este usuario");
        }
        UserProfile profile = UserProfile.builder()
                .userId(userId)
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .birthDate(dto.getBirthDate())
                .build();
        return mapToDTO(repository.save(profile));
    }

    public UserProfileDTO updateProfile(String userId, UserProfileDTO dto) {
        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));

        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setBirthDate(dto.getBirthDate());

        return mapToDTO(repository.save(profile));
    }

    private UserProfileDTO mapToDTO(UserProfile profile) {
        return UserProfileDTO.builder()
                .fullName(profile.getFullName())
                .email(profile.getEmail())
                .birthDate(profile.getBirthDate())
                .build();
    }
}
