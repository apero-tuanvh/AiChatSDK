package com.apero.service.data.remote.repository

import com.apero.service.data.remote.mapper.toListStyleGenImage
import com.apero.service.data.remote.mapper.toModel
import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.request.ChatSseRequest
import com.apero.service.data.remote.model.request.GenImageRequest
import com.apero.service.data.remote.service.AiChatService
import com.apero.service.data.remote.service.ChatSSEService
import com.apero.service.domain.model.ChatAnswerData
import com.apero.service.domain.model.ConversationModel
import com.apero.service.domain.model.GenImageResult
import com.apero.service.domain.model.GenImageStyleModel
import com.apero.service.domain.repository.AiChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import okio.FileSystem
import okio.Path

internal class AiChatRepositoryImpl(
    private val aiChatService: AiChatService,
    private val chatSSEService: ChatSSEService,
    private val fileSystem: FileSystem
) : AiChatRepository {

    private val _genImageStyleModelFlow = MutableStateFlow<List<GenImageStyleModel>>(emptyList())

    override suspend fun uploadFile(
        file: Path,
        applicationCode: String,
        botCode: String
    ): ApiResult<String> {
        val fileName = file.name
        val fileBytes = fileSystem.read(file) {
            readByteArray()
        }
        val response = aiChatService.uploadFile(
            botCode = botCode,
            fileName = fileName,
            fileBytes = fileBytes,
            applicationCode = applicationCode
        )
        return when (response) {
            is ApiResult.Success -> ApiResult.Success(response.data.data.filePath)
            is ApiResult.Error -> response
        }
    }

    override suspend fun generateImage(
        botCode: String,
        request: String,
        conversationId: String,
        positivePrompt: String,
        persist: Boolean
    ): ApiResult<GenImageResult> {
        val genImageRequest = GenImageRequest(
            question = request,
            persist = persist,
            conversationId = conversationId,
            positivePrompt = positivePrompt,
        )
        return when (val response = aiChatService.generateImage(botCode, genImageRequest)) {
            is ApiResult.Success -> {
                ApiResult.Success(response.data.toModel())
            }

            is ApiResult.Error -> {
                response
            }
        }
    }

    override suspend fun updateGenImage(
        botCode: String,
        answerId: String,
        isShow: Boolean
    ): ApiResult<Boolean> {
        return when (val response = aiChatService.updateGenImage(botCode, answerId, isShow)) {
            is ApiResult.Success -> {
                ApiResult.Success(response.data.data?.isShow == isShow)
            }

            is ApiResult.Error -> {
                response
            }
        }
    }

    override suspend fun fetchGenImageStyleModels(botCode: String): ApiResult<List<GenImageStyleModel>> {
        when (val response = aiChatService.getListStyleGenImage(botCode)) {
            is ApiResult.Success -> {
                val result = response.data.toListStyleGenImage()
                _genImageStyleModelFlow.update { result }
                return ApiResult.Success(result)
            }

            is ApiResult.Error -> {
                return response
            }
        }
    }

    override fun getFlowGenImageStyleModel(): Flow<List<GenImageStyleModel>> {
        return _genImageStyleModelFlow.asStateFlow()
    }

    override fun chatSeeAsFlow(
        botCode: String,
        question: String,
        fileUrls: List<String>,
        persist: Boolean,
        conversationId: String
    ): Flow<ChatAnswerData> = channelFlow {
        trySend(ChatAnswerData.Init(conversationId))
        val chatSeeRequest = ChatSseRequest(
            question = question,
            fileUrls = fileUrls,
            persist = persist,
            conversationId = conversationId
        )
        var message = ""
        val chatSseResponse = chatSSEService.sendMessage(botCode, chatSeeRequest)
        chatSseResponse.distinctUntilChanged().collect { response ->
            message += response.message
            trySend(ChatAnswerData.Answering(conversationId, message))
        }

        trySend(ChatAnswerData.Completed(conversationId, null))
    }
}