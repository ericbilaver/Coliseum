package com.coliseum.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform