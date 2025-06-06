package com.apero.service.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SignUpRequest(
    @SerialName("referId") val referId: String,  // deviceId or user identifier
    @SerialName("applicationCode") val applicationCode: String = "ai-virtu"
)
