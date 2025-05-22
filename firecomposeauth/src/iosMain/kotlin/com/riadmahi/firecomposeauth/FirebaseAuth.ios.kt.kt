package com.riadmahi.firecomposeauth

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseCore.FIRApp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume
import kotlinx.cinterop.*

@OptIn(ExperimentalForeignApi::class)
class IOSFirebaseAuth : FireComposeAuth {
    private var auth: FIRAuth? = null

    override fun initialize(context: Any?) {
        if (FIRApp.defaultApp() == null) {
            FIRApp.configure()
        }
        auth = FIRAuth.auth()
    }

    override suspend fun login(email: String, password: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            if (auth == null) throw Exception("Firebase not initialized")
            try {
                auth!!.signInWithEmail(email, password) { result, error ->
                    if (error != null) {
                        cont.resume(AuthResult.Error(error.localizedDescription))
                    } else {
                        val user = result?.user()
                        cont.resume(AuthResult.Success(AuthUser(user?.uid() ?: "", user?.email())))
                    }
                }
            } catch (e: Throwable) {
                cont.resume(AuthResult.Error(e.message ?: "Unknown error"))
            }
        }

    override suspend fun register(email: String, password: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            if (auth == null) throw Exception("Firebase not initialized")
            try {
                auth!!.createUserWithEmail(email, password) { result, error ->
                    if (error != null) {
                        cont.resume(AuthResult.Error(error.localizedDescription))
                    } else {
                        val user = result?.user()
                        cont.resume(AuthResult.Success(AuthUser(user?.uid() ?: "", user?.email())))
                    }
                }
            } catch (e: Throwable) {
                cont.resume(AuthResult.Error(e.message ?: "Unknown error"))
            }
        }

    override suspend fun logout() {
        if (auth == null) throw Exception("Firebase not initialized")
        try {
            memScoped {
                val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                val success = auth!!.signOut(errorPtr.ptr)
                if (!success) {
                    val error = errorPtr.value
                    AuthResult.Error(error?.localizedDescription ?: "Unknown sign out error")
                }
            }
        } catch (e: Throwable) {
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    override fun currentUser(): AuthUser? {
        if (auth == null) return null
        val user = auth!!.currentUser()
        return user?.let { AuthUser(it.uid(), it.email()) }
    }
}

actual val fireComposeAuth: FireComposeAuth
    get() = IOSFirebaseAuth()