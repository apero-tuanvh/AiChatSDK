package com.apero.service.data.remote.service

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.request.RefreshTokenRequest
import com.apero.service.data.remote.model.request.SignUpRequest
import com.apero.service.data.remote.model.response.RefreshTokenResponse
import com.apero.service.data.remote.model.response.SignUpResponse
import com.apero.service.extension.safePost
import io.ktor.client.HttpClient

interface AuthApiService {
    suspend fun signUp(request: SignUpRequest): ApiResult<SignUpResponse>
    suspend fun refreshToken(request: RefreshTokenRequest): ApiResult<RefreshTokenResponse>
}

class AuthApiServiceImpl(
    private val client: HttpClient
) : AuthApiService {

    override suspend fun signUp(request: SignUpRequest): ApiResult<SignUpResponse> =
        client.safePost("api/v1/auth/sign-up", request)

    override suspend fun refreshToken(request: RefreshTokenRequest): ApiResult<RefreshTokenResponse> =
        client.safePost("api/v1/auth/refresh", request)
}

