package com.apero.service.domain.model

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val message: String,
        val code: Int? = null,
        val rawBody: String? = null,
        val errorResponse: ErrorResponse? = null,
        val errorCodeEnum: ErrorCode? = null
    ) : ApiResult<Nothing>()
}
