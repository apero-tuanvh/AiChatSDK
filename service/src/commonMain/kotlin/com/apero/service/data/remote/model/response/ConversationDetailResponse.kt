package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConversationDetailResponse(
    @SerialName("data")
    val data: ConversationData,

    @SerialName("timestamp")
    val timestamp: String,

    @SerialName("path")
    val path: String,

    @SerialName("traceId")
    val traceId: String
)

@Serializable
internal data class ConversationData(
    @SerialName("items")
    val items: List<MessageItem>,

    @SerialName("cursor")
    val cursor: String?,

    @SerialName("hasNext")
    val hasNext: Boolean
)

@Serializable
internal data class MessageItem(
    @SerialName("id")
    val id: String,

    @SerialName("content")
    val content: String,

    @SerialName("attachment")
    val attachment: String?,

    @SerialName("botId")
    val botId: String,

    @SerialName("userId")
    val userId: String,

    @SerialName("conversationId")
    val conversationId: String,

    @SerialName("sendFrom")
    val sendFrom: SendFrom,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("isShow")
    val isShow: Boolean,

    @SerialName("topic")
    val topic: String?
)

@Serializable
internal enum class SendFrom {
    @SerialName("USER")
    USER,

    @SerialName("ASSISTANT")
    ASSISTANT
}
