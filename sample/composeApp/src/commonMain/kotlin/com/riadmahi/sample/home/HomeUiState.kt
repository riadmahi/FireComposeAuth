package com.riadmahi.sample.home

sealed interface HomeUiState {
    data object Idle : HomeUiState
    data object SignedOut : HomeUiState
    data class Error(val message: String) : HomeUiState
}