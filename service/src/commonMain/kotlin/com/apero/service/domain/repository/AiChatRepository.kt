package com.apero.service.domain.repository

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.domain.model.GenImageResult
import com.apero.service.domain.model.GenImageStyleModel
import kotlinx.coroutines.flow.Flow
import okio.Path


interface AiChatRepository {
    suspend fun uploadFile(
        file: Path,
        applicationCode: String,
        botCode: String,
    ): ApiResult<String>

    suspend fun generateImage(
        botCode: String,
        request: String,
        conversationId: String,
        positivePrompt: String,
        persist: Boolean = true,
    ): ApiResult<GenImageResult>

    suspend fun updateGenImage(
        botCode: String,
        answerId: String,
        isShow: Boolean,
    ): ApiResult<Boolean>

    suspend fun fetchGenImageStyleModels(botCode: String): ApiResult<List<GenImageStyleModel>>
    fun getFlowGenImageStyleModel(): Flow<List<GenImageStyleModel>>
}