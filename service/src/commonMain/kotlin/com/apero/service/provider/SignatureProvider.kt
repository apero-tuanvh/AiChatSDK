package com.apero.service.provider

import com.apero.service.data.remote.model.SignatureData

internal expect class SignatureProvider() {
    fun parse(apiKey: String, publicKey: String, timestamp: Long): Result<SignatureData>
}