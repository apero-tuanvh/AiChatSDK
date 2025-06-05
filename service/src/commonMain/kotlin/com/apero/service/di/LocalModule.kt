package com.apero.service.di

import com.apero.service.data.local.LocalStorage
import com.apero.service.provider.DeviceIdProvider
import com.apero.service.provider.SignatureProvider
import com.russhwolf.settings.Settings
import okio.FileSystem
import okio.SYSTEM

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

    internal val deviceIdProvider: DeviceIdProvider by lazy {
        return@lazy DeviceIdProvider(localStorage)
    }

    internal val fileSystem by lazy {
        FileSystem.SYSTEM
    }
}