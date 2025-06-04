package com.apero.service.logger

import platform.Foundation.NSLog

actual class Logger {
    actual fun d(tag: String, message: String) {
        NSLog("DEBUG: [$tag] $message")
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        NSLog("ERROR: [$tag] $message \n ${throwable?.message}")
    }
}