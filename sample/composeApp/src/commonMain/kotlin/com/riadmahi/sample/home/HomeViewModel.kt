package com.riadmahi.sample.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadmahi.firecomposeauth.FireComposeAuth
import kotlinx.coroutines.launch

class HomeViewModel(private val auth: FireComposeAuth) : ViewModel() {
    var uiState by mutableStateOf<HomeUiState>(HomeUiState.Idle)
        private set

    fun signOut() {
        viewModelScope.launch {
            try {
                auth.logout()
                uiState = HomeUiState.SignedOut
            } catch (e: Exception) {
                uiState = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}