package com.apero.service.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshTokenRequest(
    @SerialName("refreshToken")
    val refreshToken: String
)