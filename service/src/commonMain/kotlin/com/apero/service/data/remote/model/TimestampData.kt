package com.apero.service.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TimestampData(
    val timestamp: Long
)

@Serializable
data class TimestampResponseWrapper(
    val data: TimestampData,
    val timestamp: String,
    val path: String,
    val traceId: String
)
