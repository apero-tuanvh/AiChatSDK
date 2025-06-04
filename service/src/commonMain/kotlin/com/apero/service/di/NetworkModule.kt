package com.apero.service.di

import com.apero.service.data.remote.repository.TimestampRepositoryImpl
import com.apero.service.data.remote.service.TimestampService
import com.apero.service.data.remote.service.TimestampServiceImpl
import com.apero.service.domain.repository.TimestampRepository
import com.apero.service.domain.usecase.GetTimestampUseCase
import com.apero.service.network.HttpClientFactory
import io.ktor.client.HttpClient

internal object NetworkModule {
    private val httpClientProvider by lazy {
        HttpClientFactory()
    }

    internal val timestampHttpClient by lazy {
        httpClientProvider.createTimeStampHttpClient()
    }

    internal val timestampService: TimestampService by lazy {
        return@lazy TimestampServiceImpl(timestampHttpClient)
    }

    internal val timestampRepository: TimestampRepository by lazy {
        return@lazy TimestampRepositoryImpl(timestampService)
    }

    internal val timestampUseCase: GetTimestampUseCase by lazy {
        return@lazy GetTimestampUseCase(timestampRepository)
    }

}