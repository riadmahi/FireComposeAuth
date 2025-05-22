package com.riadmahi.firecomposeauth

import cocoapods.FirebaseAuth.FIRAuth
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume
import kotlinx.cinterop.*

@OptIn(ExperimentalForeignApi::class)
class IOSFirebaseAuth : FireComposeAuth {
    private val auth = FIRAuth.auth()

    override suspend fun login(email: String, password: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            auth.signInWithEmail(email, password) { result, error ->
                if (error != null) {
                    cont.resume(AuthResult.Error(error.localizedDescription))
                } else {
                    val user = result?.user()
                    cont.resume(AuthResult.Success(AuthUser(user?.uid() ?: "", user?.email())))
                }
            }
        }

    override suspend fun register(email: String, password: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            auth.createUserWithEmail(email, password) { result, error ->
                if (error != null) {
                    cont.resume(AuthResult.Error(error.localizedDescription))
                } else {
                    val user = result?.user()
                    cont.resume(AuthResult.Success(AuthUser(user?.uid() ?: "", user?.email())))
                }
            }
        }

    override suspend fun logout() {
        memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            val success = auth.signOut(errorPtr.ptr)
            if (!success) {
                val error = errorPtr.value
                throw Exception(error?.localizedDescription ?: "Unknown sign out error")
            }
        }
    }

    override fun currentUser(): AuthUser? {
        val user = auth.currentUser()
        return user?.let { AuthUser(it.uid(), it.email()) }
    }
}

actual val fireComposeAuth: FireComposeAuth
    get() = IOSFirebaseAuth()