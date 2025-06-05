package com.apero.service.network

import com.apero.service.AiChatSDK
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpSend

internal actual class HttpClientFactory {
    actual fun createTimeStampHttpClient(): HttpClient {
        return createBaseHttpClient(Darwin)
    }

    actual fun createAuthHttpClient(): HttpClient {
        return createBaseHttpClient(Darwin) {
            install(AiChatSDK.signatureInterceptor)
        }
    }
}