package com.apero.service.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StyleListResponse(
    @SerialName("data")
    val data: StyleDataResponse?,

    @SerialName("timestamp")
    val timestamp: String?,

    @SerialName("path")
    val path: String?,

    @SerialName("traceId")
    val traceId: String?,
)

@Serializable
data class StyleDataResponse(
    @SerialName("items")
    val items: List<StyleEntity>?,

    @SerialName("meta")
    val meta: MetaDataResponse?,
)

@Serializable
data class MetaDataResponse(
    @SerialName("pagination")
    val pagination: PaginationData?,
)

@Serializable
data class PaginationData(
    @SerialName("page")
    val page: Int?,

    @SerialName("pageSize")
    val pageSize: Int?,

    @SerialName("pageCount")
    val pageCount: Int?,

    @SerialName("total")
    val total: Int?,
)

@Serializable
data class StyleEntity(
    @SerialName("id")
    val id: Int?,

    @SerialName("documentId")
    val documentId: String?,

    @SerialName("order")
    val order: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("positivePrompt")
    val positivePrompt: String?,

    @SerialName("createdAt")
    val createdAt: String?,

    @SerialName("updatedAt")
    val updatedAt: String?,

    @SerialName("publishedAt")
    val publishedAt: String?,

    @SerialName("images")
    val images: List<ImageEntity>?,
)

@Serializable
data class ImageEntity(
    @SerialName("id")
    val id: Int?,

    @SerialName("documentId")
    val documentId: String?,

    @SerialName("url")
    val url: String?,

    @SerialName("isUrlSigned")
    val isUrlSigned: Boolean?,
)
