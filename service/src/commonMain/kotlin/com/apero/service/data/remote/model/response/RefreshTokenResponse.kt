package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    @SerialName("data") val data: TokenData,
    @SerialName("timestamp") val timestamp: String,
    @SerialName("path") val path: String,
    @SerialName("traceId") val traceId: String
) {
    @Serializable
    data class TokenData(
        @SerialName("accessToken") val accessToken: String,
        @SerialName("refreshToken") val refreshToken: String
    )
}
