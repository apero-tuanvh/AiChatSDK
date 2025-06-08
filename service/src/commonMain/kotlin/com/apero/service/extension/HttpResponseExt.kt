package com.apero.service.extension

import com.apero.service.AiChatSDK
import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.ErrorCode
import com.apero.service.data.remote.model.ErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
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
    try {
        val response = post(url) {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
        AiChatSDK.logger.d(AiChatSDK.TAG_FOR_DEBUG + "POST", "data: ${response.bodyAsText()}")
        if (response.status.isSuccess()) {
            ApiResult.Success(response.body<R>())
        } else {
            response.handleErrorResponse()
        }
    } catch (e: ClientRequestException) {
        e.response.handleErrorResponse()
    } catch (e: ServerResponseException) {
        e.response.handleErrorResponse()
    } catch (e: Exception) {
        ApiResult.Error("Network or unknown error: ${e.message}", null)
    }
}

suspend inline fun <reified R> HttpClient.safeGet(
    url: String,
    queryParams: Map<String, String> = emptyMap()
): ApiResult<R> = withContext(Dispatchers.IO) {
    try {
        val response = get(url) {
            url {
                queryParams.forEach { (k, v) ->
                    parameters.append(k, v)
                }
            }
        }
        AiChatSDK.logger.d(AiChatSDK.TAG_FOR_DEBUG + "GET", "data: ${response.bodyAsText()}")
        if (response.status.isSuccess()) {
            ApiResult.Success(response.body<R>())
        } else {
            response.handleErrorResponse()
        }
    } catch (e: ClientRequestException) {
        e.response.handleErrorResponse()
    } catch (e: ServerResponseException) {
        e.response.handleErrorResponse()
    } catch (e: Exception) {
        ApiResult.Error("Network or unknown error: ${e.message}", null)
    }
}

// Safe DELETE
suspend inline fun <reified R> HttpClient.safeDelete(
    url: String
): ApiResult<R> = withContext(Dispatchers.IO) {
    try {
        val response = delete(url)
        if (response.status.isSuccess()) {
            ApiResult.Success(response.body<R>())
        } else {
            response.handleErrorResponse()
        }
    } catch (e: ClientRequestException) {
        e.response.handleErrorResponse()
    } catch (e: ServerResponseException) {
        e.response.handleErrorResponse()
    } catch (e: Exception) {
        ApiResult.Error("Network or unknown error: ${e.message}", null)
    }
}

// Safe POST File Multipart
suspend inline fun <reified R> HttpClient.safePostFile(
    url: String,
    fileName: String,
    fileBytes: ByteArray,
    formFields: Map<String, String> = emptyMap()
): ApiResult<R> = withContext(Dispatchers.IO) {
    try {
        val multipartData = MultiPartFormDataContent(
            formData {
                append("file", fileBytes, Headers.build {
                    append(
                        HttpHeaders.ContentDisposition,
                        "form-data; name=\"file\"; filename=\"$fileName\""
                    )
                })
                formFields.forEach { (key, value) ->
                    append(key, value)
                }
            }
        )

        val response = post(url) {
            setBody(multipartData)
        }

        if (response.status.isSuccess()) {
            ApiResult.Success(response.body<R>())
        } else {
            response.handleErrorResponse()
        }
    } catch (e: ClientRequestException) {
        e.response.handleErrorResponse()
    } catch (e: ServerResponseException) {
        e.response.handleErrorResponse()
    } catch (e: Exception) {
        ApiResult.Error("Network or unknown error: ${e.message}", null)
    }
}

suspend inline fun <reified R> HttpClient.safePatchForm(
    url: String,
    formFields: Map<String, String>
): ApiResult<R> = withContext(Dispatchers.IO) {
    try {
        val response = patch(url) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                formFields.forEach { (k, v) -> append(k, v) }
            }))
        }
        if (response.status.isSuccess()) {
            ApiResult.Success(response.body<R>())
        } else {
            response.handleErrorResponse()
        }
    } catch (e: ClientRequestException) {
        e.response.handleErrorResponse()
    } catch (e: ServerResponseException) {
        e.response.handleErrorResponse()
    } catch (e: Exception) {
        ApiResult.Error("Network or unknown error: ${e.message}", null)
    }
}