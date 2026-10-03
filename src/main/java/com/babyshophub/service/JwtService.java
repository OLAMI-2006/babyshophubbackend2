package com.babyshophub.service;

import com.babyshophub.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    private final JwtEncoder encoder;
    private final long expirationSeconds;

    public JwtService(JwtEncoder encoder, @Value("${JWT_EXPIRATION_SECONDS:3600}") long expirationSeconds) {
        if (expirationSeconds < 60) throw new IllegalArgumentException("JWT expiry must be at least 60 seconds");
        this.encoder = encoder;
        this.expirationSeconds = expirationSeconds;
    }

    public String issueToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("babyshophub")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .subject(user.getEmail())
                .claim("roles", user.getRoles().stream().map(Enum::name).toList())
                .build();
        return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }

    public long getExpirationSeconds() { return expirationSeconds; }
}
