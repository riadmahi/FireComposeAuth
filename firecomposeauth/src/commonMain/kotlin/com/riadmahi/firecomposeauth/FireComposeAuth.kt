package com.riadmahi.firecomposeauth

interface FireComposeAuth {
    fun initialize(context: Any? = null)
    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(email: String, password: String): AuthResult
    suspend fun logout()
    fun currentUser(): AuthUser?
}

expect val fireComposeAuth : FireComposeAuth