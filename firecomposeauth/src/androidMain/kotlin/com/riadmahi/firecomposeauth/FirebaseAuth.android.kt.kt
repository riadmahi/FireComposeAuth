package com.riadmahi.firecomposeauth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AndroidFirebaseAuth: FireComposeAuth {
    private var auth: FirebaseAuth? = null

    override fun initialize(context: Any?) {
        val ctx = context as? android.content.Context
        if (ctx != null && com.google.firebase.FirebaseApp.getApps(ctx).isEmpty()) {
            com.google.firebase.FirebaseApp.initializeApp(ctx)
        }
        auth = FirebaseAuth.getInstance()
    }

    override suspend fun login(email: String, password: String): AuthResult {
        if (auth == null) throw Exception("Firebase not initialized")
        return try {
            val result = auth!!.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(AuthUser(result.user!!.uid, result.user!!.email))
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun register(email: String, password: String): AuthResult {
        if (auth == null) throw Exception("Firebase not initialized")
        return try {
            val result = auth!!.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success(AuthUser(result.user!!.uid, result.user!!.email))
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun logout() {
        if (auth == null) throw Exception("Firebase not initialized")
        auth!!.signOut()
    }

    override fun currentUser(): AuthUser? {
        if (auth == null) throw Exception("Firebase not initialized")
        val user = auth!!.currentUser
        return user?.let { AuthUser(it.uid, it.email) }
    }
}

actual val fireComposeAuth: FireComposeAuth
    get() = AndroidFirebaseAuth()