package com.riadmahi.sample.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadmahi.firecomposeauth.AuthResult
import com.riadmahi.firecomposeauth.FireComposeAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val auth: FireComposeAuth
) : ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RegisterUiState.Loading
            val result = auth.register(email.trim(), password)

            if (result is AuthResult.Success) {
                _uiState.value = RegisterUiState.Success
            } else {
                _uiState.value = RegisterUiState.Error((result as AuthResult.Error).message ?: "Unknown error")
            }
        }
    }
}