package com.apero.service.domain.repository

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.domain.model.ConversationDetail
import com.apero.service.domain.model.ConversationHistory
import com.apero.service.domain.model.ConversationModel

interface ConversationRepository {
    suspend fun getConversationList(
        botCode: String,
        page: Int = 0,
        limit: Int = 10
    ): ApiResult<ConversationHistory>

    suspend fun getConversationDetail(
        botCode: String,
        conversationId: String,
    ): ApiResult<ConversationDetail>

    suspend fun deleteConversation(
        botCode: String,
        conversationId: String
    ): ApiResult<Boolean>

    suspend fun createConversation(botCode: String): ApiResult<ConversationModel>
}