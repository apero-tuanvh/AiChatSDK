package com.apero.service.data.remote.exception

class CancellationMessageException : Exception() {
    override val message: String
        get() = "Cancel message"
}