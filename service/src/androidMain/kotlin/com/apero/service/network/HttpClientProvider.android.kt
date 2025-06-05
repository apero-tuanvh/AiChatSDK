package com.apero.service.network

import com.apero.service.di.NetworkModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

internal actual class HttpClientFactory {
    actual fun createTimeStampHttpClient(): HttpClient {
        return createBaseHttpClient(OkHttp)
    }

    actual fun createAuthHttpClient(): HttpClient {
        return createBaseHttpClient(OkHttp) {
            install(NetworkModule.signatureInterceptor)
        }
    }

    actual fun createChatHttpClient(): HttpClient {
        return createBaseHttpClient(OkHttp) {
            install(NetworkModule.signatureInterceptor)
            install(NetworkModule.authInterceptor)
        }
    }
}