package com.apero.service.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

internal actual class HttpClientFactory {
    actual fun createTimeStampHttpClient(): HttpClient {
        return createBaseHttpClient(OkHttp)
    }
}