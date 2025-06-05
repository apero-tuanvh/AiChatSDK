package com.apero.service.data.local

import com.apero.service.AiChatSDK
import com.russhwolf.settings.Settings

class LocalStorage(private val settings: Settings) {

    var accessToken: String?
        get() = settings.getStringOrNull(KEY_ACCESS_TOKEN)
        set(value) {
            if (value != null) {
                settings.putString(KEY_ACCESS_TOKEN, value)
            } else {
                AiChatSDK.logger.e(AiChatSDK.TAG_FOR_DEBUG, "accessToken is null", null)
            }
        }

    var refreshToken: String?
        get() = settings.getStringOrNull(KEY_REFRESH_TOKEN)
        set(value) {
            if (value != null) {
                settings.putString(KEY_REFRESH_TOKEN, value)
            } else {
                AiChatSDK.logger.e(AiChatSDK.TAG_FOR_DEBUG, "refreshToken is null", null)
            }
        }

    companion object {
        private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
    }
}