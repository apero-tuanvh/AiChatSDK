package com.apero.service.data.remote.mapper

import com.apero.service.data.remote.model.response.GenImageResponse
import com.apero.service.domain.model.GenImageResult

internal fun GenImageResponse.toModel(): GenImageResult {
    return GenImageResult(
        id = this.data?.id ?: "",
        content = this.data?.content,
        attachment = this.data?.attachment,
        botId = this.data?.botId,
        userId = this.data?.userId,
        conversationId = this.data?.conversationId,
        sendFrom = this.data?.sendFrom,
        createdAt = this.data?.createdAt,
        isShow = this.data?.isShow,
        topic = this.data?.topic,
        questionId = this.data?.questionId,
        timestamp = this.timestamp,
        path = this.path,
        traceId = this.traceId,
    )
}