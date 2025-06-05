package com.apero.service.data.remote.model

enum class ErrorCode(val code: String) {
    INVALID_INPUT("ERR100"),
    UNAUTHORIZED("ERR102"),
    INTERNAL_SERVER_ERROR("ERR103"),
    ERROR_KEY_OPENAI("ERR104"),
    TIMEOUT_ERROR("ERR105"),
    NOT_FOUND("ERR106"),
    FORBIDDEN("ERR107"),
    TOKEN_EXPIRED("ERR108"),
    INVALID_TOKEN("ERR109"),
    MISSING_REQUIRED_FIELDS("ERR110"),
    RESOURCE_ALREADY_EXISTS("ERR111"),
    ERROR_KEY_GEMINI("ERR112"),
    INVALID_CHAT_FORMAT("ERR113"),
    TOO_MANY_REQUESTS("ERR114"),
    MODE_EXISTS("ERR115"),
    PAGE_LIMIT_EXCEEDED("ERR116"),
    INVALID_OPERATION("ERR117"),
    FAIL_IN_HOUSE("ERR118"),
    REQUEST_CANCELLED("REQUEST_CANCELLED"),
    USER_SIGN_UP_FAILED("ERR119");

    companion object {
        fun fromCode(code: String?): ErrorCode? = entries.find { it.code == code }
    }
}