package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DeleteConversationResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String? = null
)
