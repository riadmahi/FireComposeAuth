package com.riadmahi.firecomposeauth

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRAuthDataResult
import cocoapods.FirebaseAuth.FIREmailAuthProvider
import cocoapods.FirebaseAuth.FIREmailAuthProviderID
import cocoapods.FirebaseAuth.FIRGoogleAuthProvider
import cocoapods.FirebaseAuth.FIROAuthProvider
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
                            cont.resume(
                                AuthResult.Error(
                                    error.localizedDescription,
                                    error.code.toInt().mapNSErrorCodeToFirebaseErrorCode()
                                )
                            )
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
                        cont.resume(
                            AuthResult.Error(
                                error.localizedDescription,
                                error.code.toInt().mapNSErrorCodeToFirebaseErrorCode()
                            )
                        )
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
        } catch (e: Throwable) {
            AuthResult.Error(e.message)
        }
    }

    override suspend fun refreshUser(): AuthUser? =
        suspendCancellableCoroutine { cont ->
            val user = auth.currentUser()
            if (user == null) {
                cont.resume(null)
                return@suspendCancellableCoroutine
            }

            user.reloadWithCompletion { error ->
                if (error != null) {
                    cont.resume(null)
                } else {
                    cont.resume(AuthUser(user.uid(), user.email()))
                }
            }
        }

    override suspend fun sendEmailVerification(): AuthResult  =
        suspendCancellableCoroutine { cont ->
            val user = auth.currentUser()
            if (user == null) {
                cont.resume(AuthResult.Error(errorCode = FirebaseAuthErrorCodes.ERROR_USER_NOT_FOUND))
                return@suspendCancellableCoroutine
            }

            user.sendEmailVerificationWithCompletion { error ->
                if (error != null) {
                    cont.resume(AuthResult.Error(error.localizedDescription, error.code.toInt().mapNSErrorCodeToFirebaseErrorCode()))
                } else {
                    cont.resume(AuthResult.Success)
                }
            }
        }

    override suspend fun reauthenticate(email: String, password: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            val user = auth.currentUser()
            if (user == null) {
                cont.resume(AuthResult.Error(FirebaseAuthErrorCodes.ERROR_USER_NOT_FOUND))
                return@suspendCancellableCoroutine
            }
            val credential = FIREmailAuthProvider.credentialWithEmail(email = email, password = password)
            user.reauthenticateWithCredential(credential) { result, error ->
                if (error != null) {
                    cont.resume(AuthResult.Error(
                        errorCode = error.code.toInt().mapNSErrorCodeToFirebaseErrorCode(),
                        message = error.localizedDescription
                    ))
                } else {
                    cont.resume(AuthResult.Success)
                }
            }
        }

    override suspend fun deleteAccount(): AuthResult =
        suspendCancellableCoroutine { cont ->
            val user = auth.currentUser()
            if (user == null) {
                cont.resume(AuthResult.Error(FirebaseAuthErrorCodes.ERROR_USER_NOT_FOUND))
                return@suspendCancellableCoroutine
            }

            user.deleteWithCompletion { error ->
                if (error != null) {
                    cont.resume(AuthResult.Error(
                        errorCode = error.code.toInt().mapNSErrorCodeToFirebaseErrorCode(),
                        message = error.localizedDescription
                    ))
                } else {
                    cont.resume(AuthResult.Success)
                }
            }
        }

    override fun isEmailVerified(): Boolean {
        val user = auth.currentUser()
        return user?.isEmailVerified() ?: false
    }

    override fun currentUser(): AuthUser? {
        val user = auth.currentUser()
        return user?.let { AuthUser(it.uid(), it.email()) }
    }

    override suspend fun sendPasswordResetEmail(email: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            auth.sendPasswordResetWithEmail(email) { error ->
                if (error != null) {
                    cont.resume(
                        AuthResult.Error(
                            error.localizedDescription,
                            error.code.toInt().mapNSErrorCodeToFirebaseErrorCode()
                        )
                    )
                } else {
                    cont.resume(AuthResult.Success)
                }
            }
        }

    override suspend fun signInWithGoogle(idToken: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            val credential = FIRGoogleAuthProvider.credentialWithIDToken(idToken, accessToken = "")
            auth.signInWithCredential(credential) { _, error ->
                if (error != null) {
                    cont.resume(AuthResult.Error(
                        errorCode = error.code.toInt().mapNSErrorCodeToFirebaseErrorCode(),
                        message = error.localizedDescription
                    ))
                } else {
                    cont.resume(AuthResult.Success)
                }
            }
        }

    override suspend fun signInWithApple(idToken: String): AuthResult =
        suspendCancellableCoroutine { cont ->
            val credential = FIROAuthProvider.appleCredentialWithIDToken(
                idToken = idToken,
                rawNonce = null,
                fullName = null
            )
            auth.signInWithCredential(credential) { _, error ->
                if (error != null) {
                    cont.resume(AuthResult.Error(
                        errorCode = error.code.toInt().mapNSErrorCodeToFirebaseErrorCode(),
                        message = error.localizedDescription
                    ))
                } else {
                    cont.resume(AuthResult.Success)
                }
            }
        }
}

fun Int?.mapNSErrorCodeToFirebaseErrorCode(): String {
    return when (this) {
        17008 -> FirebaseAuthErrorCodes.ERROR_INVALID_EMAIL
        17009 -> FirebaseAuthErrorCodes.ERROR_WRONG_PASSWORD
        17011 -> FirebaseAuthErrorCodes.ERROR_USER_NOT_FOUND
        17010 -> FirebaseAuthErrorCodes.ERROR_TOO_MANY_REQUESTS
        17007 -> FirebaseAuthErrorCodes.ERROR_EMAIL_ALREADY_IN_USE
        17026 -> FirebaseAuthErrorCodes.ERROR_WEAK_PASSWORD
        else -> FirebaseAuthErrorCodes.UNKNOWN
    }
}

actual fun getFireComposeAuth(context: Any?): FireComposeAuth = IOSFirebaseAuth()
