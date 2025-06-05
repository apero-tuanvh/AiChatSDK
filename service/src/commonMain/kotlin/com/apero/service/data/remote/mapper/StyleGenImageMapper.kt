package com.apero.service.data.remote.mapper

import com.apero.service.data.remote.model.response.StyleEntity
import com.apero.service.data.remote.model.response.StyleListResponse
import com.apero.service.domain.model.GenImageStyleModel

fun StyleListResponse.toListStyleGenImage(): List<GenImageStyleModel> {
    val data = data?.items
    return data?.mapNotNull {
        it.toStyleGenImage()
    } ?: emptyList()
}

fun StyleEntity.toStyleGenImage(): GenImageStyleModel? {
    val url = images?.firstOrNull()?.url
    return if (id != null && name != null && url != null && positivePrompt != null) {
        GenImageStyleModel(
            idStyle = id,
            nameStyle = name,
            image = url,
            prompt = positivePrompt
        )
    } else {
        null
    }
}
