package com.apero.service.data.remote.service

import com.apero.service.AiChatSDK
import com.apero.service.data.remote.exception.CancellationMessageException
import com.apero.service.data.remote.model.request.ChatSseRequest
import com.apero.service.data.remote.model.response.ChatSseResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.TimeSource

internal interface ChatSSEService {
    fun sendMessage(botCode: String, chatSseRequest: ChatSseRequest): Flow<ChatSseResponse>
    fun cancelSendSseMessage()
}

internal class ChatSSEServiceImpl(
    private val chatSSEClient: HttpClient
) : ChatSSEService {
    private val _eventCancelSendSseMessage =
        MutableSharedFlow<Boolean>(replay = 0, extraBufferCapacity = 1)

    override fun sendMessage(
        botCode: String,
        chatSseRequest: ChatSseRequest
    ): Flow<ChatSseResponse> = channelFlow {
        val parentJob = Job()
        val scope = CoroutineScope(Dispatchers.IO + parentJob)
        var callApi: HttpClientCall? = null
        val startMark = TimeSource.Monotonic.markNow()
        scope.launch {
            chatSSEClient.sse(
                urlString = "api/v1/ai-virtu/$botCode/ask-sse",
                showCommentEvents = true,
                showRetryEvents = true,
                request = {
                    method = HttpMethod.Post
                    contentType(ContentType.Application.Json)
                    setBody(chatSseRequest)
                }
            ) {
                this.incoming.collect { event ->
                    val receiveMark = TimeSource.Monotonic.markNow()
                    AiChatSDK.logger.d(
                        AiChatSDK.TAG_FOR_DEBUG + "Time to receive SSE event",
                        "⏳ Receive SSE event after: ${receiveMark - startMark}"
                    )
                    callApi = this.call
                    val rawData = event.data
                    if (!rawData.isNullOrBlank()) {
                        val jsonContent = rawData.removePrefix("data: ").trim()
                        try {
                            val message = Json.decodeFromString<ChatSseResponse>(jsonContent)
                            trySend(message)
                        } catch (_: Exception) {
                        }
                    }
                }
                AiChatSDK.logger.d(AiChatSDK.TAG_FOR_DEBUG, "SSE stream completed")
                this@channelFlow.close()
            }
        }

        scope.launch {
            _eventCancelSendSseMessage.collect {
                if (it) {
                    callApi?.cancel()
                    parentJob.cancel()
                    AiChatSDK.logger.d(
                        AiChatSDK.TAG_FOR_DEBUG,
                        "Cancel SSE message"
                    )
                    this@channelFlow.close(CancellationMessageException())
                }
            }
        }

        awaitClose {
            callApi?.cancel()
            parentJob.cancel()
        }
    }

    override fun cancelSendSseMessage() {
        _eventCancelSendSseMessage.tryEmit(true)
    }


}