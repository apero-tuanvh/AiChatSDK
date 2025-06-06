package com.apero.service.data.remote.service

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.request.GenImageRequest
import com.apero.service.data.remote.model.response.ConversationDetailResponse
import com.apero.service.data.remote.model.response.ConversationHistoryResponse
import com.apero.service.data.remote.model.response.ConversationResponse
import com.apero.service.data.remote.model.response.DeleteConversationResponse
import com.apero.service.data.remote.model.response.FileUploadResponse
import com.apero.service.data.remote.model.response.GenImageResponse
import com.apero.service.data.remote.model.response.StyleListResponse
import com.apero.service.extension.safeDelete
import com.apero.service.extension.safeGet
import com.apero.service.extension.safePatchForm
import com.apero.service.extension.safePost
import com.apero.service.extension.safePostFile
import io.ktor.client.HttpClient

internal interface AiChatService {

    suspend fun getConversationHistory(
        botCode: String,
        page: Int,
        limit: Int = 10
    ): ApiResult<ConversationHistoryResponse>

    suspend fun uploadFile(
        botCode: String,
        fileName: String,
        fileBytes: ByteArray,
        applicationCode: String
    ): ApiResult<FileUploadResponse>

    suspend fun getConversationDetail(
        botCode: String,
        conversationId: String
    ): ApiResult<ConversationDetailResponse>

    suspend fun deleteConversation(
        botCode: String,
        conversationId: String
    ): ApiResult<DeleteConversationResponse>

    suspend fun createConversation(botCode: String): ApiResult<ConversationResponse>

    suspend fun getListStyleGenImage(botCode: String): ApiResult<StyleListResponse>

    suspend fun generateImage(
        botCode: String,
        request: GenImageRequest
    ): ApiResult<GenImageResponse>

    suspend fun updateGenImage(
        botCode: String,
        answerId: String,
        isShow: Boolean
    ): ApiResult<GenImageResponse>
}

internal class AiChatServiceImpl(
    private val client: HttpClient
) : AiChatService {
    override suspend fun getConversationHistory(
        botCode: String,
        page: Int,
        limit: Int
    ): ApiResult<ConversationHistoryResponse> {
        return client.safeGet(
            url = "api/v1/ai-virtu/$botCode-message/history",
            queryParams = mapOf(
                "page" to page.toString(),
                "limit" to limit.toString()
            )
        )
    }

    override suspend fun uploadFile(
        botCode: String,
        fileName: String,
        fileBytes: ByteArray,
        applicationCode: String
    ): ApiResult<FileUploadResponse> {
        return client.safePostFile(
            url = "api/v1/ai-virtu/$botCode/upload",
            fileName = fileName,
            fileBytes = fileBytes,
            formFields = mapOf("applicationCode" to applicationCode)
        )
    }

    override suspend fun getConversationDetail(
        botCode: String,
        conversationId: String
    ): ApiResult<ConversationDetailResponse> {
        return client.safeGet(
            url = "api/v1/ai-virtu/$botCode-message/$conversationId"
        )
    }

    override suspend fun deleteConversation(
        botCode: String,
        conversationId: String
    ): ApiResult<DeleteConversationResponse> {
        return client.safeDelete(
            url = "api/v1/ai-virtu/$botCode-message/$conversationId"
        )
    }

    override suspend fun createConversation(botCode: String): ApiResult<ConversationResponse> {
        return client.safePost(
            url = "api/v1/ai-virtu/$botCode-message/conversation",
            requestBody = Unit
        )
    }

    override suspend fun getListStyleGenImage(botCode: String): ApiResult<StyleListResponse> {
        return client.safeGet(
            url = "api/v1/ai-virtu/$botCode/list-styles"
        )
    }

    override suspend fun generateImage(
        botCode: String,
        request: GenImageRequest
    ): ApiResult<GenImageResponse> {
        return client.safePost(
            url = "api/v2/ai-virtu/$botCode/gen-image",
            requestBody = request
        )
    }

    override suspend fun updateGenImage(
        botCode: String,
        answerId: String,
        isShow: Boolean
    ): ApiResult<GenImageResponse> {
        return client.safePatchForm(
            url = "api/v1/ai-virtu/$botCode/$answerId",
            formFields = mapOf("isShow" to isShow.toString())
        )
    }


}
