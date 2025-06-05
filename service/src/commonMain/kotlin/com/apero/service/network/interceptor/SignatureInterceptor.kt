package com.apero.service.network.interceptor

import com.apero.service.AiChatSDK
import com.apero.service.domain.usecase.GetTimestampUseCase
import com.apero.service.provider.SignatureProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.util.AttributeKey

internal class SignatureInterceptor(
    private val apiKey: String,
    private val publicKey: String,
    private val signatureProvider: SignatureProvider,
    private val timestampUseCase: GetTimestampUseCase,
) : HttpClientPlugin<Unit, SignatureInterceptor> {

    override val key: AttributeKey<SignatureInterceptor> = AttributeKey("SignatureInterceptor")

    override fun prepare(block: Unit.() -> Unit) = this

    override fun install(plugin: SignatureInterceptor, scope: HttpClient) {
        scope.requestPipeline.intercept(HttpRequestPipeline.Transform) {
            val timestamp = timestampUseCase()
            val result = signatureProvider.parse(apiKey, publicKey, timestamp).getOrElse {
                AiChatSDK.logger.e(AiChatSDK.TAG_FOR_DEBUG, "Failed to parse signature", it)
                null
            }
            result?.let { signatureData ->
                context.headers.append("x-api-signature", signatureData.signature)
                context.headers.append("x-api-keyid", signatureData.keyId)
                context.headers.append("x-api-timestamp", signatureData.timeStamp.toString())
            }

            proceedWith(subject)
        }
    }
}
