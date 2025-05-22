package com.riadmahi.firecomposeauth

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform