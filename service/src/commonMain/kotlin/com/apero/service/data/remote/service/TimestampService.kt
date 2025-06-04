package com.apero.service.data.remote.service

import com.apero.service.domain.model.ApiResult
import com.apero.service.domain.model.ErrorCode
import com.apero.service.domain.model.ErrorResponse
import com.apero.service.domain.model.TimestampResponseWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json


interface TimestampService {
    suspend fun getTimestamp(): ApiResult<TimestampResponseWrapper>
}

class TimestampServiceImpl(
    private val client: HttpClient
) : TimestampService {
    override suspend fun getTimestamp(): ApiResult<TimestampResponseWrapper> {
        return try {
            val response = client.get("api/timestamp")
            ApiResult.Success(response.body())
        } catch (e: ClientRequestException) {
            handleErrorResponse(e.response)
        } catch (e: ServerResponseException) {
            handleErrorResponse(e.response)
        } catch (e: Exception) {
            ApiResult.Error(
                message = "Network or unknown error: ${e.message}",
                rawBody = null
            )
        }
    }

    private suspend fun handleErrorResponse(response: HttpResponse): ApiResult.Error {
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
}
