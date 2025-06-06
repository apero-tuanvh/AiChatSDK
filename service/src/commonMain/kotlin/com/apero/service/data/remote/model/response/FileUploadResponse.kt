package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FileUploadResponse(
    @SerialName("data")
    val data: UploadData,

    @SerialName("timestamp")
    val timestamp: String,

    @SerialName("path")
    val path: String,

    @SerialName("traceId")
    val traceId: String
)

@Serializable
internal data class UploadData(
    @SerialName("filePath")
    val filePath: String
)
