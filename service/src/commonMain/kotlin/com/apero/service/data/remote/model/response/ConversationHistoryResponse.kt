package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConversationHistoryResponse(
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
    internal data class ConversationHistoryData(
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
    internal data class ConversationItem(
        @SerialName("id") val id: String,
        @SerialName("userId") val userId: String,
        @SerialName("botId") val botId: String,
        @SerialName("createdAt") val createdAt: String,
        @SerialName("updatedAt") val updatedAt: String,
        @SerialName("modeId") val modeId: String? = null,
        @SerialName("threadId") val threadId: String? = null,
        @SerialName("idCharacter") val idCharacter: String? = null,
        @SerialName("BotMessages") val botMessages: List<BotMessageResponse>,
    )
}
