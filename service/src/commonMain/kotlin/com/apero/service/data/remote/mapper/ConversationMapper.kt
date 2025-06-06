package com.apero.service.data.remote.mapper

import com.apero.service.data.remote.model.response.BotMessageResponse
import com.apero.service.data.remote.model.response.ConversationDetailResponse
import com.apero.service.data.remote.model.response.ConversationHistoryResponse
import com.apero.service.data.remote.model.response.ConversationResponse
import com.apero.service.data.remote.model.response.SendFrom
import com.apero.service.domain.model.BotMessageModel
import com.apero.service.domain.model.ConversationDetail
import com.apero.service.domain.model.ConversationHistory
import com.apero.service.domain.model.ConversationModel

internal fun ConversationResponse.toModel(): ConversationModel {
    return ConversationModel(
        data = this.data.toModel(),
        timestamp = this.timestamp,
        path = this.path,
        traceId = this.traceId
    )
}

internal fun ConversationResponse.ConversationData.toModel(): ConversationModel.ConversationData {
    return ConversationModel.ConversationData(
        id = this.id,
        userId = this.userId,
        botId = this.botId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

internal fun ConversationHistoryResponse.toConversationHistory(): ConversationHistory {
    return ConversationHistory(
        data = ConversationHistory.ConversationHistoryData(
            items = this.data.items.map { item ->
                ConversationHistory.ConversationItem(
                    id = item.id,
                    userId = item.userId,
                    botId = item.botId,
                    createdAt = item.createdAt,
                    updatedAt = item.updatedAt,
                    botMessages = item.botMessages.map { it.toModel() }
                )
            },
            currentPage = this.data.currentPage,
            totalPages = this.data.totalPages,
            hasNextPage = this.data.hasNextPage,
            hasPreviousPage = this.data.hasPreviousPage
        ),
        timestamp = this.timestamp,
        path = this.path,
        traceId = this.traceId
    )
}

internal fun ConversationDetailResponse.toConversationDetail(): ConversationDetail {
    return ConversationDetail(
        id = this.traceId,
        messages = this.data.items.map { item ->
            ConversationDetail.ChatMessage(
                id = item.id,
                content = item.content,
                attachment = item.attachment,
                botId = item.botId,
                userId = item.userId,
                conversationId = item.conversationId,
                sendFrom = if (item.sendFrom == SendFrom.USER) ConversationDetail.MessageSender.USER else ConversationDetail.MessageSender.BOT,
                createdAt = item.createdAt,
                isShow = item.isShow,
                topic = item.topic,
            )
        },
        timestamp = this.timestamp
    )
}

internal fun BotMessageResponse.toModel(): BotMessageModel {
    return BotMessageModel(
        id = this.id,
        content = this.content,
        attachment = this.attachment,
        botId = this.botId,
        userId = this.userId,
        conversationId = this.conversationId,
        sendFrom = this.sendFrom,
        createdAt = this.createdAt,
        isShow = this.isShow,
        topic = this.topic
    )
}