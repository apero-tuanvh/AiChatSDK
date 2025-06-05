package com.apero.service.domain.model

data class UploadedFiles(
    val urls: List<String>
) {
    constructor(vararg urls: String) : this(urls.toList())
}