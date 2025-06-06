package com.apero.service.provider

import com.apero.service.data.local.LocalStorage

internal expect class DeviceIdProvider(localStorage: LocalStorage) {
    fun getOrCreateUUID(): String
}