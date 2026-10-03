package com.babyshophub.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresInSeconds) {}
