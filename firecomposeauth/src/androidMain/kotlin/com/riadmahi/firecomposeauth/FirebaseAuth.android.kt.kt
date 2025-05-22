package com.riadmahi.firecomposeauth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AndroidFirebaseAuth: FireComposeAuth {
    private val auth = Firebase.auth

    override suspend fun login(email: String, password: String): AuthResult = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        AuthResult.Success(AuthUser(result.user!!.uid, result.user!!.email))
    } catch (e: Exception) {
        AuthResult.Error(e.message ?: "Unknown error")
    }

    override suspend fun register(email: String, password: String): AuthResult = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        AuthResult.Success(AuthUser(result.user!!.uid, result.user!!.email))
    } catch (e: Exception) {
        AuthResult.Error(e.message ?: "Unknown error")
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun currentUser(): AuthUser? {
        val user = auth.currentUser
        return user?.let { AuthUser(it.uid, it.email) }
    }
}

actual val fireComposeAuth: FireComposeAuth
    get() = AndroidFirebaseAuth()