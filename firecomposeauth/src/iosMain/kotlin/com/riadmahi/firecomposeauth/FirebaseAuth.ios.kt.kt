package com.riadmahi.firecomposeauth

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRAuthDataResult
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
    private var auth: FIRAuth

    init {
        if (FIRApp.defaultApp() == null) {
            FIRApp.configure()
        }
        auth = FIRAuth.auth()
    }

    override suspend fun login(email: String, password: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            try {
                auth.signInWithEmail(
                    email = email,
                    password = password,
                    completion = { _: FIRAuthDataResult?, error: NSError? ->
                        if (error != null) {
                            cont.resume(AuthResult.Error(error.localizedDescription, error.code.toInt()))
                        } else {
                            cont.resume(
                                AuthResult.Success
                            )
                        }
                    })
            } catch (e: Throwable) {
                cont.resume(AuthResult.Error(e.message))
            }
        }

    override suspend fun register(email: String, password: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            try {
                auth.createUserWithEmail(email, password) { result, error ->
                    if (error != null) {
                        cont.resume(AuthResult.Error(error.localizedDescription, error.code.toInt()))
                    } else {
                        cont.resume(AuthResult.Success)
                    }
                }
            } catch (e: Throwable) {
                cont.resume(AuthResult.Error(e.message))
            }
        }

    override suspend fun logout() {
        try {
            memScoped {
                val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                val success = auth.signOut(errorPtr.ptr)
                if (!success) {
                    val error = errorPtr.value
                    AuthResult.Error(error?.localizedDescription)
                }
            }
        }
        catch (e: Throwable) {
            AuthResult.Error(e.message)
        }
    }

    override fun currentUser(): AuthUser? {
        val user = auth.currentUser()
        return user?.let { AuthUser(it.uid(), it.email()) }
    }

    override suspend fun sendPasswordResetEmail(email: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            auth.sendPasswordResetWithEmail(email) { error ->
                if (error != null) {
                    cont.resume(AuthResult.Error(error.localizedDescription, error.code.toInt()))
                } else {
                    cont.resume(AuthResult.Success)
                }
            }
        }
}

actual fun getFireComposeAuth(context: Any?): FireComposeAuth = IOSFirebaseAuth()
