package com.riadmahi.firecomposeauth

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AndroidFirebaseAuth(private val context: Any?): FireComposeAuth {
    private val auth: FirebaseAuth

    init {
        val ctx = context as? android.content.Context
        if (ctx != null && FirebaseApp.getApps(ctx).isEmpty()) {
            FirebaseApp.initializeApp(ctx)
        }
        auth = FirebaseAuth.getInstance()
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(AuthUser(result.user!!.uid, result.user!!.email))
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun register(email: String, password: String): AuthResult {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success(AuthUser(result.user!!.uid, result.user!!.email))
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun currentUser(): AuthUser? {
        val user = auth.currentUser
        return user?.let { AuthUser(it.uid, it.email) }
    }
}

actual fun getFireComposeAuth(context: Any?): FireComposeAuth = AndroidFirebaseAuth(context)