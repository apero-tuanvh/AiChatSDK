package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenImageResponse(
    @SerialName("success") val success: Boolean?,
    @SerialName("message") val message: String?,
    @SerialName("data") val data: GenImageData?,
    @SerialName("timestamp") val timestamp: String?,
    @SerialName("path") val path: String?,
    @SerialName("traceId") val traceId: String?,
)

@Serializable
internal data class GenImageData(
    @SerialName("id") val id: String,
    @SerialName("content") val content: String?,
    @SerialName("attachment") val attachment: String?,
    @SerialName("botId") val botId: String?,
    @SerialName("userId") val userId: String?,
    @SerialName("conversationId") val conversationId: String?,
    @SerialName("sendFrom") val sendFrom: String?,
    @SerialName("createdAt") val createdAt: String?,
    @SerialName("isShow") val isShow: Boolean?,
    @SerialName("topic") val topic: String?,
    @SerialName("questionId") val questionId: String?,
)
