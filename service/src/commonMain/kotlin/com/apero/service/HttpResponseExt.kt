package com.apero.service

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.ErrorCode
import com.apero.service.data.remote.model.ErrorResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json


suspend fun HttpResponse.handleErrorResponse(): ApiResult.Error {
    val response = this
    val rawBody = response.bodyAsText()
    val errorResponse = try {
        Json.decodeFromString<ErrorResponse>(rawBody)
    } catch (_: Exception) {
        null
    }

    val errorCodeEnum = ErrorCode.fromCode(errorResponse?.errorCode)

    val message = errorResponse?.message
        ?: "Unknown error, HTTP code: ${response.status.value}"

    return ApiResult.Error(
        message = message,
        code = response.status.value,
        rawBody = rawBody,
        errorResponse = errorResponse,
        errorCodeEnum = errorCodeEnum
    )
}