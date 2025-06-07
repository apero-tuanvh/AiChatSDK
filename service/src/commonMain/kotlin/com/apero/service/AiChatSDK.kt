package com.apero.service

import com.apero.service.data.local.LocalStorage
import com.apero.service.di.LocalModule
import com.apero.service.di.NetworkModule
import com.apero.service.domain.repository.AiChatRepository
import com.apero.service.domain.repository.ConversationRepository
import com.apero.service.domain.usecase.GetTimestampUseCase
import com.apero.service.domain.usecase.RefreshTokenUseCase
import com.apero.service.domain.usecase.SignUpUseCase
import com.apero.service.logger.Logger

object AiChatSDK {

    const val TAG_FOR_DEBUG = "FOR_TESTER_IN_AiChatSDK: "

    private var baseUrl: String? = null
    private var bundleId: String? = null
    private var apiKey: String? = null
    private var publicKey: String? = null
    internal var isDebug: Boolean = true
    private var applicationCode: String? = null

    val logger = Logger()


    internal fun getBaseUrl(): String {
        return baseUrl
            ?: throw IllegalArgumentException("You need install BaseUrl with AiChatSDK.install() in your application")
    }

    internal fun getBundleId(): String {
        return bundleId
            ?: throw IllegalArgumentException("You need install BundleId with AiChatSDK.install() in your application")
    }

    internal fun getApiKey(): String {
        return apiKey
            ?: throw IllegalArgumentException("You need install ApiKey with AiChatSDK.install() in your application")
    }

    internal fun getPublicKey(): String {
        return publicKey
            ?: throw IllegalArgumentException("You need install PublicKey with AiChatSDK.install() in your application")
    }

    internal fun getApplicationCode(): String {
        return applicationCode
            ?: throw IllegalArgumentException("You need install ApplicationCode with AiChatSDK.install() in your application")
    }

    fun install(
        baseUrl: String,
        bundleId: String,
        apiKey: String,
        publicKey: String,
        isDebug: Boolean = true,
        applicationCode: String = "ai-virtu"
    ) {
        this.baseUrl = baseUrl
        this.bundleId = bundleId
        this.apiKey = apiKey
        this.publicKey = publicKey
        this.isDebug = isDebug
        this.applicationCode = applicationCode
    }

    val timestampUseCase: GetTimestampUseCase by lazy {
        return@lazy NetworkModule.timestampUseCase
    }

    val signupUseCase: SignUpUseCase by lazy {
        return@lazy NetworkModule.signupUseCase
    }

    val refreshTokenUseCase: RefreshTokenUseCase by lazy {
        return@lazy NetworkModule.refreshTokenUseCase
    }

    val localStorage: LocalStorage by lazy {
        return@lazy LocalModule.localStorage
    }

    val aiChatRepository: AiChatRepository by lazy {
        return@lazy NetworkModule.aiChatRepository
    }

    val conversationRepository: ConversationRepository by lazy {
        return@lazy NetworkModule.conversationRepository
    }


}