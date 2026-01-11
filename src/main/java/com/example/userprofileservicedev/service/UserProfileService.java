package com.example.userprofileservicedev.service;

import com.example.userprofileservicedev.constants.MessageKeys;
import com.example.userprofileservicedev.domain.UserProfile;
import com.example.userprofileservicedev.dto.CreateProfileRequest;
import com.example.userprofileservicedev.dto.ProfileResponse;
import com.example.userprofileservicedev.dto.UpdateProfileRequest;
import com.example.userprofileservicedev.exception.ConflictException;
import com.example.userprofileservicedev.exception.NotFoundException;
import com.example.userprofileservicedev.mapper.UserProfileMapper;
import com.example.userprofileservicedev.repository.UserProfileRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileService {

    private final UserProfileRepository repository;
    private final UserProfileMapper mapper;
    private final MessageSource messageSource;

    public UserProfileService(UserProfileRepository repository, UserProfileMapper mapper, MessageSource messageSource) {
        this.repository = repository;
        this.mapper = mapper;
        this.messageSource = messageSource;
    }

    public ProfileResponse getMyProfile(String userId) {
        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(getMessage(MessageKeys.ERROR_PROFILE_NOT_FOUND)));
        return mapper.toResponse(profile);
    }

    public ProfileResponse createMyProfile(String userId, CreateProfileRequest req) {
        if (repository.existsByUserId(userId)) {
            throw new ConflictException(getMessage(MessageKeys.ERROR_PROFILE_CONFLICT));
        }
        if (repository.existsByEmail(req.getEmail())) {
            throw new ConflictException(getMessage(MessageKeys.ERROR_PROFILE_CONFLICT));
        }
        UserProfile profile = mapper.fromCreate(userId, req);
        return mapper.toResponse(repository.save(profile));
    }

    public ProfileResponse putMyProfile(String userId, UpdateProfileRequest req) {
        UserProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(getMessage(MessageKeys.ERROR_PROFILE_NOT_FOUND)));

        if (isEmailOwnedByDifferentUser(userId, req.getEmail())) {
            throw new ConflictException(getMessage(MessageKeys.ERROR_PROFILE_CONFLICT));
        }

        mapper.applyPut(profile, req);

        return mapper.toResponse(repository.save(profile));
    }

    private boolean isEmailOwnedByDifferentUser(String userId, String email) {
        return repository.findByEmail(email)
                .map(existing -> !existing.getUserId().equals(userId))
                .orElse(false);
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
