package com.apero.service.domain.model

data class SignatureData(
    val signature: String, 
    val keyId: String, 
    val timeStamp: Long
)