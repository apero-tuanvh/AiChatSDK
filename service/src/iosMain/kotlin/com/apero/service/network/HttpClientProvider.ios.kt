package com.apero.service.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

internal actual class HttpClientFactory {
    actual fun createTimeStampHttpClient(): HttpClient {
        return createBaseHttpClient(Darwin)
    }
}