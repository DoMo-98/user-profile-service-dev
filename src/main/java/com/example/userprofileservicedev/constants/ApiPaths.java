package com.example.userprofileservicedev.constants;

/**
 * Constants for API endpoint routes.
 */
public final class ApiPaths {

    private ApiPaths() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Base paths
    public static final String API_V1 = "/api/v1";

    // Auth endpoints
    public static final String AUTH = API_V1 + "/auth";
    public static final String AUTH_LOGIN = "/login";

    // Profile endpoints
    public static final String PROFILE = API_V1 + "/profile";

    // Token types
    public static final String BEARER_TOKEN_TYPE = "Bearer";
}

