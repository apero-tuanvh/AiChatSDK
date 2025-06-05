package com.apero.service.domain.repository

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.domain.model.AuthResult

interface AuthRepository {
    suspend fun signUp(
        referId: String,
        applicationCode: String
    ): ApiResult<AuthResult>

    suspend fun refreshToken(refreshToken: String): ApiResult<AuthResult>
}

