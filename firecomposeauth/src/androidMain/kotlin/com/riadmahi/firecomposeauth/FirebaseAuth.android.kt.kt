package com.riadmahi.firecomposeauth

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
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
            AuthResult.Error(e.message, e.errorCode)
        } catch (e: Exception) {
            AuthResult.Error(e.message)
        }
    }

    override suspend fun register(email: String, password: String): AuthResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(e.message, e.errorCode)
        }
        catch (e: Exception) {
            AuthResult.Error(e.message)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun refreshUser(): AuthUser? {
        val user = auth.currentUser ?: return null
        user.reload().await()
        return user.let { AuthUser(it.uid, it.email) }
    }

    override suspend fun sendEmailVerification(): AuthResult {
        val user = auth.currentUser ?: return AuthResult.Error(errorCode = FirebaseAuthErrorCodes.ERROR_USER_NOT_FOUND)
        return try {
            user.sendEmailVerification().await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message)
        }
    }

    override suspend fun reauthenticate(email: String, password: String): AuthResult {
        val user = auth.currentUser ?: return AuthResult.Error(FirebaseAuthErrorCodes.ERROR_USER_NOT_FOUND)
        return try {
            val credential = EmailAuthProvider.getCredential(email, password)
            user.reauthenticate(credential).await()
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(e.errorCode)
        } catch (e: Exception) {
            AuthResult.Error(FirebaseAuthErrorCodes.UNKNOWN)
        }
    }

    override suspend fun deleteAccount(): AuthResult {
        val user = auth.currentUser ?: return AuthResult.Error(FirebaseAuthErrorCodes.ERROR_USER_NOT_FOUND)
        return try {
            user.delete().await()
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(e.errorCode)
        } catch (e: Exception) {
            AuthResult.Error(FirebaseAuthErrorCodes.UNKNOWN)
        }
    }

    override fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
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
            AuthResult.Error(e.message, e.errorCode)
        } catch (e: Exception) {
            AuthResult.Error(e.message)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            auth.signInWithCredential(credential).await()
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(e.errorCode)
        } catch (e: Exception) {
            AuthResult.Error(FirebaseAuthErrorCodes.UNKNOWN)
        }
    }

    override suspend fun signInWithApple(idToken: String): AuthResult {
        val credential = OAuthProvider.newCredentialBuilder("apple.com")
            .setIdToken(idToken)
            .build()
        return try {
            auth.signInWithCredential(credential).await()
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(e.errorCode)
        } catch (e: Exception) {
            AuthResult.Error(FirebaseAuthErrorCodes.UNKNOWN)
        }
    }

    override suspend fun updatePassword(newPassword: String): AuthResult {
        return try {
            auth.currentUser?.updatePassword(newPassword)?.await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message, (e as? FirebaseAuthException)?.errorCode)
        }
    }
}

actual fun getFireComposeAuth(context: Any?): FireComposeAuth = AndroidFirebaseAuth(context)