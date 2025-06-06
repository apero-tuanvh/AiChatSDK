package com.apero.service.data.remote.service

import com.apero.service.AiChatSDK
import com.apero.service.data.remote.model.request.ChatSseRequest
import com.apero.service.data.remote.model.response.ChatSseResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal interface ChatSSEService {
    fun sendMessage(botCode: String, chatSseRequest: ChatSseRequest): Flow<ChatSseResponse>
}

internal class ChatSSEServiceImpl(
    private val chatSSEClient: HttpClient
) : ChatSSEService {

    override fun sendMessage(
        botCode: String,
        chatSseRequest: ChatSseRequest
    ): Flow<ChatSseResponse> = channelFlow {
        withContext(Dispatchers.IO) {
            val url = "api/v1/ai-virtu/$botCode/ask-sse"
            chatSSEClient.sse(
                urlString = url,
                showCommentEvents = true,
                showRetryEvents = true,
                request = {
                    method = HttpMethod.Post
                    contentType(ContentType.Application.Json)
                    setBody(chatSseRequest)
                }) {
                this.incoming.collect { event ->
                    val rawData = event.data
                    if (!rawData.isNullOrBlank()) {
                        val jsonContent = rawData.removePrefix("data: ").trim()
                        try {
                            val message = Json.decodeFromString<ChatSseResponse>(jsonContent)
                            trySend(message)
                        } catch (e: Exception) {
                            println("Parse error: $e")
                        }

                    }
                }

            }
        }
    }


}