package com.apero.service.provider

import com.apero.service.domain.model.SignatureData

internal expect class SignatureProvider() {
    fun parse(apiKey: String, publicKey: String, timestamp: Long): Result<SignatureData>
}