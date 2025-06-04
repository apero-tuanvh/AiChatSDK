package com.apero.chataisdk

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform