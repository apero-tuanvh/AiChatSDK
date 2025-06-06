package com.apero.service.data.remote.repository

import com.apero.service.AiChatSDK
import com.apero.service.data.local.LocalStorage
import com.apero.service.data.remote.mapper.toAuthResult
import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.request.RefreshTokenRequest
import com.apero.service.data.remote.model.request.SignUpRequest
import com.apero.service.data.remote.model.response.RefreshTokenResponse
import com.apero.service.data.remote.model.response.SignUpResponse
import com.apero.service.data.remote.service.AuthApiService
import com.apero.service.domain.model.AuthResult
import com.apero.service.domain.repository.AuthRepository

internal class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val localStorage: LocalStorage,
) : AuthRepository {
    override suspend fun signUp(referId: String, applicationCode: String): ApiResult<AuthResult> {
        val request = SignUpRequest(referId, applicationCode)
        return when (val response = apiService.signUp(request)) {
            is ApiResult.Success -> {
                val authResult = response.data.toAuthResult()
                localStorage.accessToken = authResult.accessToken
                localStorage.refreshToken = authResult.refreshToken
                ApiResult.Success(response.data.toAuthResult())
            }

            is ApiResult.Error -> {
                AiChatSDK.logger.e(
                    AiChatSDK.TAG_FOR_DEBUG,
                    "Error during signUp by ${response.message}",
                )
                response
            }
        }
    }

    override suspend fun refreshToken(refreshToken: String): ApiResult<AuthResult> {
        val request = RefreshTokenRequest(refreshToken)
        return when (val response = apiService.refreshToken(request)) {
            is ApiResult.Success -> {
                val authResult = response.data.toAuthResult()
                localStorage.accessToken = authResult.accessToken
                localStorage.refreshToken = authResult.refreshToken
                ApiResult.Success(response.data.toAuthResult())
            }

            is ApiResult.Error -> {
                AiChatSDK.logger.e(
                    AiChatSDK.TAG_FOR_DEBUG,
                    "Error during refreshToken by ${response.message}",
                )
                response
            }
        }
    }

}
