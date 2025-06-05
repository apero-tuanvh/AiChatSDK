package com.apero.service.domain.model

data class ConversationModel(
    val data: ConversationData,
    val timestamp: String,
    val path: String,
    val traceId: String
) {
    data class ConversationData(
        val id: String,
        val userId: String,
        val botId: String,
        val createdAt: String,
        val updatedAt: String
    )
}