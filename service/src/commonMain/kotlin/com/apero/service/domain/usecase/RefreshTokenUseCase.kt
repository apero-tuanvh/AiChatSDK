package com.apero.service.domain.usecase

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.domain.model.AuthResult
import com.apero.service.domain.repository.AuthRepository

class RefreshTokenUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(refreshToken: String): ApiResult<AuthResult> {
        return repository.refreshToken(refreshToken)
    }
}
