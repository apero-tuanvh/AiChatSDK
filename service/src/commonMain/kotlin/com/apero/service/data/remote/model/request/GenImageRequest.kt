package com.apero.service.data.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenImageRequest(
    @SerialName("question") val question: String,
    @SerialName("persist") val persist: Boolean,
    @SerialName("conversationId") val conversationId: String,
    @SerialName("positivePrompt") val positivePrompt: String,
)
