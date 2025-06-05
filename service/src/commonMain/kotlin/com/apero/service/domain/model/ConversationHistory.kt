package com.apero.service.domain.model

data class ConversationHistory(
    val data: ConversationHistoryData,
    val timestamp: String,
    val path: String,
    val traceId: String
) {
    data class ConversationHistoryData(
        val items: List<ConversationItem>,
        val currentPage: Int,
        val totalPages: Int,
        val hasNextPage: Boolean,
        val hasPreviousPage: Boolean
    )

    data class ConversationItem(
        val id: String,
        val userId: String,
        val botId: String,
        val createdAt: String,
        val updatedAt: String,
        val botMessages: List<BotMessageModel>
    )
}