package com.riadmahi.firecomposeauth

interface FireComposeAuth {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(email: String, password: String): AuthResult
    suspend fun sendPasswordResetEmail(email: String): AuthResult
    suspend fun logout()
    suspend fun refreshUser(): AuthUser?
    suspend fun sendEmailVerification(): AuthResult
    suspend fun reauthenticate(email: String, password: String): AuthResult
    suspend fun deleteAccount(): AuthResult
    fun isEmailVerified(): Boolean
    fun currentUser(): AuthUser?
    suspend fun signInWithGoogle(idToken: String): AuthResult
    suspend fun signInWithApple(idToken: String): AuthResult
}

expect fun getFireComposeAuth(context: Any? = null): FireComposeAuth