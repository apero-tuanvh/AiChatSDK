package com.apero.service.data.local

import com.apero.service.AiChatSDK
import com.russhwolf.settings.Settings

class LocalStorage(private val settings: Settings) {

    internal var accessToken: String?
        get() = settings.getStringOrNull(KEY_ACCESS_TOKEN)
        set(value) {
            if (value != null) {
                settings.putString(KEY_ACCESS_TOKEN, value)
            } else {
                AiChatSDK.logger.e(AiChatSDK.TAG_FOR_DEBUG, "accessToken is null", null)
            }
        }

    internal var refreshToken: String?
        get() = settings.getStringOrNull(KEY_REFRESH_TOKEN)
        set(value) {
            if (value != null) {
                settings.putString(KEY_REFRESH_TOKEN, value)
            } else {
                AiChatSDK.logger.e(AiChatSDK.TAG_FOR_DEBUG, "refreshToken is null", null)
            }
        }

    internal var deviceIdUser: String?
        get() = settings.getStringOrNull(KEY_USER_ID)
        set(value) {
            if (value != null) {
                settings.putString(KEY_USER_ID, value)
            } else {
                AiChatSDK.logger.e(AiChatSDK.TAG_FOR_DEBUG, "deviceIdUser is null", null)
            }
        }

    internal fun clearTokens() {
        accessToken = null
        refreshToken = null
    }

    internal companion object {
        private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
        private const val KEY_USER_ID = "KEY_USER_ID"
    }
}