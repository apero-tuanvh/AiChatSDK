package com.apero.service

import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.ErrorCode
import com.apero.service.data.remote.model.ErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
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

suspend inline fun <reified T, reified R> HttpClient.safePost(
    url: String,
    requestBody: T
): ApiResult<R> = withContext(Dispatchers.IO) {
    return@withContext try {
        val response = post(url) {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
        if (response.status.isSuccess()) {
            ApiResult.Success(response.body())
        } else {
            response.handleErrorResponse()
        }
    } catch (e: ClientRequestException) {
        e.response.handleErrorResponse()
    } catch (e: ServerResponseException) {
        e.response.handleErrorResponse()
    } catch (e: Exception) {
        ApiResult.Error(
            message = "Network or unknown error: ${e.message}",
            rawBody = null
        )
    }
}