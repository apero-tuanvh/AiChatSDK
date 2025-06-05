package com.apero.service.network.interceptor

import com.apero.service.AiChatSDK
import com.apero.service.data.local.LocalStorage
import com.apero.service.data.remote.model.ApiResult
import com.apero.service.data.remote.model.ErrorCode
import com.apero.service.domain.usecase.RefreshTokenUseCase
import com.apero.service.domain.usecase.SignUpUseCase
import com.apero.service.handleErrorResponse
import com.apero.service.provider.DeviceIdProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.takeFrom
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.util.AttributeKey

class AuthInterceptor(
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
        scope.requestPipeline.intercept(HttpReceivePipeline.After) {
            kotlin.runCatching {
                val response = subject as HttpResponse
                val apiResult = response.handleErrorResponse()
                if (response.status.value == 401 || apiResult.errorCodeEnum == ErrorCode.TOKEN_EXPIRED || apiResult.errorCodeEnum == ErrorCode.INVALID_TOKEN) {
                    val newToken = localStorage.refreshToken?.let {
                        refreshTokenUseCase(it)
                    } ?: signUpUseCase(deviceIdProvider.getOrCreateUUID(), applicationCode)

                    when (newToken) {
                        is ApiResult.Success -> {
                            val newRequest = HttpRequestBuilder().apply {
                                takeFrom(response.call.request)
                                headers.remove("Authorization")
                                headers.append(
                                    "Authorization",
                                    "Bearer ${newToken.data.accessToken}"
                                )
                            }


                            val newResponse =
                                scope.requestPipeline.execute(newRequest, newRequest.body)
                            proceedWith(newResponse)
                        }

                        is ApiResult.Error -> {
                            proceed()
                        }
                    }
                }
                else{
                    proceed()
                }
            }.getOrElse {
                AiChatSDK.logger.e(
                    AiChatSDK.TAG_FOR_DEBUG,
                    "AuthInterceptor: Error handling response",
                    it
                )
            }
        }

    }
}