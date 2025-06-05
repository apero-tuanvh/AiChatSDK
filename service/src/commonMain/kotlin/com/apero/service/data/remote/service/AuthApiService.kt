package com.apero.service.data.remote.service

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
                val response = client.post("api/v1/auth/sign-up") {
                    setBody(request)
                }
                com.apero.service.data.remote.model.ApiResult.Success(response.body())
            } catch (e: ClientRequestException) {
                e.response.handleErrorResponse()
            } catch (e: ServerResponseException) {
                e.response.handleErrorResponse()
            } catch (e: Exception) {
                com.apero.service.data.remote.model.ApiResult.Error(
                    message = "Network or unknown error: ${e.message}",
                    rawBody = null
                )
            }
        }

    override suspend fun refreshToken(request: RefreshTokenRequest): ApiResult<RefreshTokenResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = client.post("api/v1/auth/refresh") {
                    setBody(request)
                }
                com.apero.service.data.remote.model.ApiResult.Success(response.body())
            } catch (e: ClientRequestException) {
                e.response.handleErrorResponse()
            } catch (e: ServerResponseException) {
                e.response.handleErrorResponse()
            } catch (e: Exception) {
                com.apero.service.data.remote.model.ApiResult.Error(
                    message = "Network or unknown error: ${e.message}",
                    rawBody = null
                )
            }
        }
}

