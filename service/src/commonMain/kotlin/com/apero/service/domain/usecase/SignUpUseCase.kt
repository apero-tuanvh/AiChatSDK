package com.apero.service.domain.usecase

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.domain.model.AuthResult
import com.apero.service.domain.repository.AuthRepository

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(referId: String, applicationCode: String): ApiResult<AuthResult> {
        return repository.signUp(referId, applicationCode)
    }
}