package com.apero.service.domain.model

sealed class ChatAnswerData(
    open val conversationId: String,
    open val isStreaming: Boolean = false,
) {

    data class Init(
        override val conversationId: String,
    ) : ChatAnswerData(
        conversationId = conversationId,
        isStreaming = true,
    )

    data class Answering(
        override val conversationId: String,
        val message: String,
    ) : ChatAnswerData(
        conversationId = conversationId,
        isStreaming = true,
    )

    data class Completed(
        override val conversationId: String,
        val error: Throwable?,
    ) : ChatAnswerData(
        conversationId = conversationId,
        isStreaming = false,
    )
}
