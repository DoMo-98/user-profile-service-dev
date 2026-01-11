package com.example.userprofileservicedev.constants;

/**
 * Constantes para las rutas de los endpoints de la API.
 */
public final class ApiPaths {

    private ApiPaths() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
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

