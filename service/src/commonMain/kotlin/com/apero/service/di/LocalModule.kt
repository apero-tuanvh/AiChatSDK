package com.apero.service.di

import com.apero.service.data.local.LocalStorage
import com.apero.service.provider.SignatureProvider
import com.russhwolf.settings.Settings

internal object LocalModule {
    private val settings: Settings by lazy {
        Settings()
    }
    internal val localStorage: LocalStorage by lazy {
        LocalStorage(settings)
    }

    internal val signatureProvider: SignatureProvider by lazy {
        return@lazy SignatureProvider()
    }
}