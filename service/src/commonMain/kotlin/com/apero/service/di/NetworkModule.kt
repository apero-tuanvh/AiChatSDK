package com.apero.service.di

import com.apero.service.AiChatSDK
import com.apero.service.AiChatSDK.getApiKey
import com.apero.service.AiChatSDK.getPublicKey
import com.apero.service.data.remote.repository.AiChatRepositoryImpl
import com.apero.service.data.remote.repository.AuthRepositoryImpl
import com.apero.service.data.remote.repository.ConversationRepositoryImpl
import com.apero.service.data.remote.repository.TimestampRepositoryImpl
import com.apero.service.data.remote.service.AiChatService
import com.apero.service.data.remote.service.AiChatServiceImpl
import com.apero.service.data.remote.service.AuthApiService
import com.apero.service.data.remote.service.AuthApiServiceImpl
import com.apero.service.data.remote.service.ChatSSEService
import com.apero.service.data.remote.service.ChatSSEServiceImpl
import com.apero.service.data.remote.service.TimestampService
import com.apero.service.data.remote.service.TimestampServiceImpl
import com.apero.service.domain.repository.AiChatRepository
import com.apero.service.domain.repository.AuthRepository
import com.apero.service.domain.repository.ConversationRepository
import com.apero.service.domain.repository.TimestampRepository
import com.apero.service.domain.usecase.GetTimestampUseCase
import com.apero.service.domain.usecase.RefreshTokenUseCase
import com.apero.service.domain.usecase.SignUpUseCase
import com.apero.service.network.HttpClientFactory
import com.apero.service.network.interceptor.AuthInterceptor
import com.apero.service.network.interceptor.SignatureInterceptor
import io.ktor.client.HttpClient

internal object NetworkModule {
    private val httpClientProvider by lazy {
        HttpClientFactory()
    }

    internal val signatureInterceptor: SignatureInterceptor by lazy {
        SignatureInterceptor(
            apiKey = getApiKey(),
            publicKey = getPublicKey(),
            timestampUseCase = AiChatSDK.timestampUseCase,
            signatureProvider = LocalModule.signatureProvider
        )
    }

    internal val authInterceptor: AuthInterceptor by lazy {
        return@lazy AuthInterceptor(
            localStorage = LocalModule.localStorage,
            signUpUseCase = signupUseCase,
            refreshTokenUseCase = refreshTokenUseCase,
            applicationCode = AiChatSDK.getApplicationCode(),
            deviceIdProvider = LocalModule.deviceIdProvider
        )
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

    private val authHttpClient by lazy {
        httpClientProvider.createAuthHttpClient()
    }

    internal val authService: AuthApiService by lazy {
        return@lazy AuthApiServiceImpl(authHttpClient)
    }

    internal val authRepository: AuthRepository by lazy {
        return@lazy AuthRepositoryImpl(authService, LocalModule.localStorage)
    }

    internal val signupUseCase: SignUpUseCase by lazy {
        return@lazy SignUpUseCase(authRepository)
    }

    internal val refreshTokenUseCase: RefreshTokenUseCase by lazy {
        return@lazy RefreshTokenUseCase(authRepository)
    }

    private val chatHttpClient by lazy {
        httpClientProvider.createChatHttpClient()
    }

    internal val aiChatService: AiChatService by lazy {
        return@lazy AiChatServiceImpl(chatHttpClient)
    }

    internal val aiChatRepository: AiChatRepository by lazy {
        return@lazy AiChatRepositoryImpl(aiChatService, chatSSEService, LocalModule.fileSystem)
    }

    internal val conversationRepository: ConversationRepository by lazy {
        return@lazy ConversationRepositoryImpl(aiChatService)
    }

    internal val chatSSEClient: HttpClient by lazy {
        httpClientProvider.createChatSSEHttpClient()
    }

    internal val chatSSEService: ChatSSEService by lazy {
        return@lazy ChatSSEServiceImpl(chatSSEClient)
    }

}