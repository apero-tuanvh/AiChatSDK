package com.apero.service.data.remote.repository

import com.apero.service.data.remote.mapper.toConversationDetail
import com.apero.service.data.remote.mapper.toConversationHistory
import com.apero.service.data.remote.mapper.toModel
import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.service.AiChatService
import com.apero.service.domain.model.ConversationDetail
import com.apero.service.domain.model.ConversationHistory
import com.apero.service.domain.model.ConversationModel
import com.apero.service.domain.repository.ConversationRepository

internal class ConversationRepositoryImpl(
    private val aiChatService: AiChatService
) : ConversationRepository {
    override suspend fun getConversationList(
        botCode: String,
        page: Int,
        limit: Int
    ): ApiResult<ConversationHistory> {
        return when (val response = aiChatService.getConversationHistory(botCode, page, limit)) {
            is ApiResult.Success -> ApiResult.Success(response.data.toConversationHistory())
            is ApiResult.Error -> response
        }
    }

    override suspend fun getConversationDetail(
        botCode: String,
        conversationId: String
    ): ApiResult<ConversationDetail> {
        return when (val response =
            aiChatService.getConversationDetail(
                botCode = botCode,
                conversationId = conversationId
            )) {
            is ApiResult.Success -> ApiResult.Success(response.data.toConversationDetail())
            is ApiResult.Error -> response
        }
    }

    override suspend fun deleteConversation(
        botCode: String,
        conversationId: String
    ): ApiResult<Boolean> {
        val response =
            aiChatService.deleteConversation(botCode = botCode, conversationId = conversationId)
        return when (response) {
            is ApiResult.Success -> ApiResult.Success(response.data.success)
            is ApiResult.Error -> response
        }
    }

    override suspend fun createConversation(botCode: String): ApiResult<ConversationModel> {
        return when (val response = aiChatService.createConversation(botCode = botCode)) {
            is ApiResult.Success -> {
                ApiResult.Success(response.data.toModel())
            }

            is ApiResult.Error -> {
                response
            }
        }
    }
}