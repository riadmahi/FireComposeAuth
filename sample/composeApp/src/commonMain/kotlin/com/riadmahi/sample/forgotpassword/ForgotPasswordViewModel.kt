package com.riadmahi.sample.forgotpassword


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadmahi.firecomposeauth.AuthResult
import com.riadmahi.firecomposeauth.FireComposeAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val auth: FireComposeAuth) : ViewModel() {
    private val _uiState = MutableStateFlow<ForgotPasswordUiState>(ForgotPasswordUiState.Idle)
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState
    private val _countdown = MutableStateFlow(0)
    val countdown: StateFlow<Int> = _countdown

    fun sendPasswordReset(email: String) {
        if (_countdown.value > 0) return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = ForgotPasswordUiState.Loading
            val result = auth.sendPasswordResetEmail(email.trim())
            if (result is AuthResult.Error) {
                _uiState.value = ForgotPasswordUiState.Error(result.message ?: "Unknown error")
            } else {
                _uiState.value = ForgotPasswordUiState.Success
                startCountdown()
            }
        }
    }

    private fun startCountdown() {
        viewModelScope.launch {
            _countdown.value = 60
            while (_countdown.value > 0) {
                delay(1000)
                _countdown.value -= 1
            }
        }
    }
}
