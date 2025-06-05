package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConversationHistoryResponse(
    @SerialName("data")
    val data: ConversationHistoryData,

    @SerialName("timestamp")
    val timestamp: String,

    @SerialName("path")
    val path: String,

    @SerialName("traceId")
    val traceId: String
) {
    @Serializable
    data class ConversationHistoryData(
        @SerialName("items")
        val items: List<ConversationItem>,

        @SerialName("currentPage")
        val currentPage: Int,

        @SerialName("totalPages")
        val totalPages: Int,

        @SerialName("hasNextPage")
        val hasNextPage: Boolean,

        @SerialName("hasPreviousPage")
        val hasPreviousPage: Boolean
    )
    
    @Serializable
    data class ConversationItem(
        @SerialName("id")
        val id: String,
        
        @SerialName("userId")
        val userId: String,
        
        @SerialName("botId")
        val botId: String,
        
        @SerialName("createdAt")
        val createdAt: String,
        
        @SerialName("updatedAt")
        val updatedAt: String,
        
        @SerialName("BotMessages")
        val botMessages: List<BotMessageResponse>
    )
}
