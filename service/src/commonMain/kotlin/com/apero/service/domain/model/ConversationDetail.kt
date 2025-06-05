package com.apero.service.domain.model

data class ConversationDetail(
    val id: String,
    val messages: List<ChatMessage>,
    val timestamp: String,
) {
    data class ChatMessage(
        val id: String,
        val content: String,
        val attachment: String?,
        val botId: String,
        val userId: String,
        val conversationId: String,
        val sendFrom: MessageSender,
        val createdAt: String,
        val isShow: Boolean,
        val topic: String?,
    )

    enum class MessageSender {
        USER,
        BOT,
        UNKNOWN;

        companion object {
            fun fromString(value: String): MessageSender {
                return when (value.uppercase()) {
                    "USER" -> USER
                    "ASSISTANT" -> BOT
                    else -> UNKNOWN
                }
            }
        }
    }
}