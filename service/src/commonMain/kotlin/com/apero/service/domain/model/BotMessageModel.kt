package com.apero.service.domain.model

data class BotMessageModel(
    val id: String,
    val content: String,
    val attachment: String?,
    val botId: String,
    val userId: String,
    val conversationId: String,
    val sendFrom: String,
    val createdAt: String,
    val isShow: Boolean,
    val topic: String
)