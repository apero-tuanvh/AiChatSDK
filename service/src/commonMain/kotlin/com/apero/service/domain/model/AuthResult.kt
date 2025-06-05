package com.apero.service.domain.model

data class AuthResult(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String?,
    val userName: String?,
    val applicationId: String,
)
