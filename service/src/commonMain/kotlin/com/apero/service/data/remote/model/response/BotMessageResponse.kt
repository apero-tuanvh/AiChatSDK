package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BotMessageResponse(
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
    val sendFrom: String,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("isShow")
    val isShow: Boolean,

    @SerialName("topic")
    val topic: String
)
