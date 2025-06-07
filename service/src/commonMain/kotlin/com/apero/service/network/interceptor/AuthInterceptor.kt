package com.apero.service.network.interceptor

import com.apero.service.AiChatSDK
import com.apero.service.data.local.LocalStorage
import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.ErrorCode
import com.apero.service.domain.usecase.RefreshTokenUseCase
import com.apero.service.domain.usecase.SignUpUseCase
import com.apero.service.extension.handleErrorResponse
import com.apero.service.provider.DeviceIdProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.takeFrom
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.AttributeKey

internal class AuthInterceptor(
    private val localStorage: LocalStorage,
    private val signUpUseCase: SignUpUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val applicationCode: String,
    private val deviceIdProvider: DeviceIdProvider
) : HttpClientPlugin<Unit, AuthInterceptor> {

    override val key: AttributeKey<AuthInterceptor> = AttributeKey("AuthInterceptor")

    override fun prepare(block: Unit.() -> Unit) = this

    override fun install(plugin: AuthInterceptor, scope: HttpClient) {
        scope.requestPipeline.intercept(HttpRequestPipeline.State) {
            localStorage.accessToken?.let { token ->
                context.headers.append("Authorization", "Bearer $token")
            }
            proceed()
        }
        scope.plugin(HttpSend).intercept { request ->
            var call = execute(request)
            runCatching {
                val response = call.response
                val refreshTokenBlock = suspend {
                    val newToken = localStorage.refreshToken?.let {
                        val result = refreshTokenUseCase(it)
                        if (result is ApiResult.Success) return@let result
                        else signUpUseCase(deviceIdProvider.getOrCreateUUID(), applicationCode)
                    } ?: signUpUseCase(deviceIdProvider.getOrCreateUUID(), applicationCode)

                    when (newToken) {
                        is ApiResult.Success -> {
                            // Gửi lại request với token mới
                            val newRequest = HttpRequestBuilder().apply {
                                takeFrom(request)
                                headers.remove("Authorization")
                                headers.append(
                                    "Authorization",
                                    "Bearer ${newToken.data.accessToken}"
                                )
                            }
                            call = execute(newRequest)
                        }

                        is ApiResult.Error -> {
                            localStorage.clearTokens()
                        }
                    }
                }

                val contentType = response.contentType()

                when {
                    response.status.value == 401 -> refreshTokenBlock()
                    response.contentType()?.match(ContentType.Application.Json) == true -> {
                        val apiResult = response.handleErrorResponse()
                        if (
                            apiResult.errorCodeEnum == ErrorCode.TOKEN_EXPIRED ||
                            apiResult.errorCodeEnum == ErrorCode.INVALID_TOKEN
                        ) {
                            refreshTokenBlock()
                        }
                    }
                }
            }.getOrElse { exception ->
                AiChatSDK.logger.e(
                    AiChatSDK.TAG_FOR_DEBUG,
                    "AuthInterceptor: Error handling response",
                    exception
                )
            }

            call
        }
    }
}