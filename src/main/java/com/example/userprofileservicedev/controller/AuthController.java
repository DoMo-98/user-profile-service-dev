package com.example.userprofileservicedev.controller;

import com.example.userprofileservicedev.dto.LoginRequest;
import com.example.userprofileservicedev.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtEncoder encoder;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.ttl}")
    private Duration ttl;

    public AuthController(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Instant now = Instant.now();
        long expiry = ttl.toSeconds();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(ttl))
                .subject(request.getUsername())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        return LoginResponse.builder()
                .accessToken(token)
                .expiresInSeconds(expiry)
                .build();
    }
}
