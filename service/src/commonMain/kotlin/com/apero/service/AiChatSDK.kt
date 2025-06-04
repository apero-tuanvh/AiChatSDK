package com.apero.service

import com.apero.service.di.NetworkModule
import com.apero.service.di.NetworkModule.timestampRepository
import com.apero.service.domain.usecase.GetTimestampUseCase
import com.apero.service.logger.Logger

object AiChatSDK {

    const val TAG_FOR_DEBUG = "FOR_TESTER_IN_AiChatSDK"

    private var baseUrl: String? = null
    private var bundleId: String? = null
    private var apiKey: String? = null
    private var publicKey: String? = null
    internal var isDebug: Boolean = true

    internal val logger = Logger()


    internal fun getBaseUrl(): String {
        return baseUrl
            ?: throw IllegalArgumentException("You need install SDK with AiChatSDK.install() in your application")
    }

    internal fun getBundleId(): String {
        return bundleId
            ?: throw IllegalArgumentException("You need install SDK with AiChatSDK.install() in your application")
    }

    internal fun getApiKey(): String {
        return apiKey
            ?: throw IllegalArgumentException("You need install SDK with AiChatSDK.install() in your application")
    }

    internal fun getPublicKey(): String {
        return publicKey
            ?: throw IllegalArgumentException("You need install SDK with AiChatSDK.install() in your application")
    }

    fun install(
        baseUrl: String,
        bundleId: String,
        apiKey: String,
        publicKey: String,
        isDebug: Boolean = true
    ) {
        this.baseUrl = baseUrl
        this.bundleId = bundleId
        this.apiKey = apiKey
        this.publicKey = publicKey
        this.isDebug = isDebug
    }

    val timestampUseCase: GetTimestampUseCase by lazy {
        return@lazy NetworkModule.timestampUseCase
    }
}