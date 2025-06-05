package com.apero.service.network

import com.apero.service.di.NetworkModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

internal actual class HttpClientFactory {
    actual fun createTimeStampHttpClient(): HttpClient {
        return createBaseHttpClient(Darwin)
    }

    actual fun createAuthHttpClient(): HttpClient {
        return createBaseHttpClient(Darwin) {
            install(NetworkModule.signatureInterceptor)
        }
    }
}