package com.apero.service.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatSseResponse(
    val isSuccess: Boolean,
    val message: String
)