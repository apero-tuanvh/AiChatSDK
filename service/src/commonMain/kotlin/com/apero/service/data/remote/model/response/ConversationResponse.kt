package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConversationResponse(
    @SerialName("data")
    val data: ConversationData,

    @SerialName("timestamp")
    val timestamp: String,

    @SerialName("path")
    val path: String,

    @SerialName("traceId")
    val traceId: String
) {
    @Serializable
    internal data class ConversationData(
        @SerialName("id")
        val id: String,

        @SerialName("userId")
        val userId: String,

        @SerialName("botId")
        val botId: String,

        @SerialName("createdAt")
        val createdAt: String,

        @SerialName("updatedAt")
        val updatedAt: String
    )
}
