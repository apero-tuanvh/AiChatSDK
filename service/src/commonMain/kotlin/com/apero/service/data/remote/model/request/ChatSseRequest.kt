package com.apero.service.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChatSseRequest(
    val question: String,
    val fileUrls: List<String> = emptyList(),
    val persist: Boolean,
    val conversationId: String
)