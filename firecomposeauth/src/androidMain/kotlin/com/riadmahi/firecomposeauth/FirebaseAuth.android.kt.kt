package com.riadmahi.firecomposeauth

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

class AndroidFirebaseAuth(context: Any?): FireComposeAuth {
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
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(e.message, e.errorCode.toInt())
        } catch (e: Exception) {
            AuthResult.Error(e.message)
        }
    }

    override suspend fun register(email: String, password: String): AuthResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(e.message, e.errorCode.toInt())
        }
        catch (e: Exception) {
            AuthResult.Error(e.message)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun currentUser(): AuthUser? {
        val user = auth.currentUser
        return user?.let { AuthUser(it.uid, it.email) }
    }

    override suspend fun sendPasswordResetEmail(email: String): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(e.message, e.errorCode.toInt())
        } catch (e: Exception) {
            AuthResult.Error(e.message)
        }
    }
}

actual fun getFireComposeAuth(context: Any?): FireComposeAuth = AndroidFirebaseAuth(context)