package com.apero.service.domain.model

@kotlinx.serialization.Serializable
data class ErrorResponse(
    val correlationId: String? = null,
    val statusCode: Int? = null,
    val timestamp: String? = null,
    val path: String? = null,
    val message: String? = null,
    val errorCode: String? = null,
    val stack: String? = null
)
