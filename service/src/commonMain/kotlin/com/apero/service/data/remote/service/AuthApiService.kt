package com.apero.service.data.remote.service

import com.apero.service.AiChatSDK
import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.request.RefreshTokenRequest
import com.apero.service.data.remote.model.request.SignUpRequest
import com.apero.service.data.remote.model.response.RefreshTokenResponse
import com.apero.service.data.remote.model.response.SignUpResponse
import com.apero.service.handleErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

interface AuthApiService {
    suspend fun signUp(request: SignUpRequest): ApiResult<SignUpResponse>
    suspend fun refreshToken(request: RefreshTokenRequest): ApiResult<RefreshTokenResponse>
}

class AuthApiServiceImpl(
    private val client: HttpClient
) : AuthApiService {

    override suspend fun signUp(request: SignUpRequest): ApiResult<SignUpResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                AiChatSDK.logger.d("signUp", "Request: $request")
                val response = client.post("api/v1/auth/sign-up") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                AiChatSDK.logger.d("signUp", "Response: ${response.bodyAsText()}")
                ApiResult.Success(response.body())
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

    override suspend fun refreshToken(request: RefreshTokenRequest): ApiResult<RefreshTokenResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = client.post("api/v1/auth/refresh") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                ApiResult.Success(response.body())
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
}

