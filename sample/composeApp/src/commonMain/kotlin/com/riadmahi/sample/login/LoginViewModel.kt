package com.riadmahi.sample.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadmahi.firecomposeauth.AuthResult
import com.riadmahi.firecomposeauth.FireComposeAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val auth: FireComposeAuth
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = LoginUiState.Loading
            val result = auth.login(email.trim(), password)

            if (result is AuthResult.Success) {
                _uiState.value = LoginUiState.Success
            } else {
                _uiState.value = LoginUiState.Error((result as AuthResult.Error).message)
            }
        }
    }
}