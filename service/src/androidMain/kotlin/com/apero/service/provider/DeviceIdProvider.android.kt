package com.apero.service.provider

import com.apero.service.data.local.LocalStorage
import java.util.UUID

actual class DeviceIdProvider actual constructor(private val localStorage: LocalStorage) {
    actual fun getOrCreateUUID(): String {
        var uuid = localStorage.deviceIdUser
        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
            localStorage.deviceIdUser = uuid
            return uuid
        } else return uuid
    }
}