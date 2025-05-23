package com.riadmahi.firecomposeauth

sealed interface AuthResult {
    data object Success : AuthResult
    data class Error(val message: String? = null, val errorCode: Int? = null) : AuthResult
}