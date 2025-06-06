package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SignUpResponse(
    @SerialName("data") val data: SignUpData,
    @SerialName("timestamp") val timestamp: String,
    @SerialName("path") val path: String,
    @SerialName("traceId") val traceId: String
) {
    @Serializable
    internal data class SignUpData(
        @SerialName("token") val token: TokenData,
        @SerialName("user") val user: UserData
    )

    // TODO: Consider making tokens @sensitive to prevent logging
    @Serializable
    internal data class TokenData(
        @SerialName("accessToken") val accessToken: String,
        @SerialName("refreshToken") val refreshToken: String
    )

    @Serializable
    internal data class UserData(
        @SerialName("id") val id: String,
        @SerialName("email") val email: String? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("applicationId") val applicationId: String
    )
}
