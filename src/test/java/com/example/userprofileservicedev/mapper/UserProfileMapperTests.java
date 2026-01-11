package com.example.userprofileservicedev.mapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserProfileMapperTests {

    private final UserProfileMapper mapper = new UserProfileMapper();

    @Test
    void whenReqIsNull_thenFromCreateReturnsNull() {
        assertNull(mapper.fromCreate("id", null));
    }

    @Test
    void whenEntityOrReqIsNull_thenApplyPutDoesNothing() {
        // No exceptions should be thrown
        mapper.applyPut(null, null);
        mapper.applyPut(null, new com.example.userprofileservicedev.dto.UpdateProfileRequest());
    }

    @Test
    void whenEntityIsNull_thenToResponseReturnsNull() {
        assertNull(mapper.toResponse(null));
    }
}
