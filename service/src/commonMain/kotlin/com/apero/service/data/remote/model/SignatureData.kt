package com.apero.service.data.remote.model

data class SignatureData(
    val signature: String, 
    val keyId: String, 
    val timeStamp: Long
)