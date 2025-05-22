package com.riadmahi.firecomposeauth

sealed interface AuthResult {
    data class Success(val user: AuthUser) : AuthResult
    data class Error(val message: String) : AuthResult
}