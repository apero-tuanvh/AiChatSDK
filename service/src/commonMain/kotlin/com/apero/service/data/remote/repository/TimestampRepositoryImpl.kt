package com.apero.service.data.remote.repository

import com.apero.service.AiChatSDK
import com.apero.service.data.remote.service.TimestampService
import com.apero.service.domain.model.ApiResult
import com.apero.service.domain.repository.TimestampRepository
import kotlinx.datetime.Clock

internal class TimestampRepositoryImpl(
    private val timestampService: TimestampService
) : TimestampRepository {
    override suspend fun getTimestamp(): Long {
        return when (val result = timestampService.getTimestamp()) {
            is ApiResult.Success -> result.data.data.timestamp
            is ApiResult.Error -> {
                AiChatSDK.logger.e(
                    AiChatSDK.TAG_FOR_DEBUG,
                    "TimestampRepository Error getting timestamp: ${result.message}"
                )
                Clock.System.now().toEpochMilliseconds()
            }
        }
    }

}