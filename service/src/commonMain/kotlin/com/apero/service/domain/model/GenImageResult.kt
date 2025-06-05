package com.apero.service.domain.model

data class GenImageResult(
    val id: String,
    val content: String?,
    val attachment: String?,
    val botId: String?,
    val userId: String?,
    val conversationId: String?,
    val sendFrom: String?,
    val createdAt: String?,
    val isShow: Boolean?,
    val topic: String?,
    val questionId: String?,
    val timestamp: String?,
    val path: String?,
    val traceId: String?,
)
