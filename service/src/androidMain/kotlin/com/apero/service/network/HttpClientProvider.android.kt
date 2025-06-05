package com.apero.service.network

import com.apero.service.AiChatSDK
import com.apero.service.network.interceptor.SignatureInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpSend

internal actual class HttpClientFactory {
    actual fun createTimeStampHttpClient(): HttpClient {
        return createBaseHttpClient(OkHttp)
    }

    actual fun createAuthHttpClient(): HttpClient {
        return createBaseHttpClient(OkHttp) {
            install(AiChatSDK.signatureInterceptor)
        }
    }
}