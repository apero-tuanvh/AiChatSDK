package com.apero.service.data.remote.mapper

import com.apero.service.data.remote.model.response.RefreshTokenResponse
import com.apero.service.data.remote.model.response.SignUpResponse
import com.apero.service.domain.model.AuthResult

internal fun SignUpResponse.toAuthResult(): AuthResult {
    return AuthResult(
        accessToken = this.data.token.accessToken,
        refreshToken = this.data.token.refreshToken,
        userId = this.data.user.id,
        email = this.data.user.email,
        userName = this.data.user.name,
        applicationId = this.data.user.applicationId,
    )
}

internal fun RefreshTokenResponse.toAuthResult(): AuthResult {
    return AuthResult(
        accessToken = this.data.accessToken,
        refreshToken = this.data.refreshToken,
        userId = "", // These fields aren't provided in refresh response
        email = null,
        userName = null,
        applicationId = "",
    )
}
