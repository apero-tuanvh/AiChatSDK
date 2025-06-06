package com.apero.service.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class TimestampData(
    val timestamp: Long
)

@Serializable
internal data class TimestampResponseWrapper(
    val data: TimestampData,
    val timestamp: String,
    val path: String,
    val traceId: String
)
