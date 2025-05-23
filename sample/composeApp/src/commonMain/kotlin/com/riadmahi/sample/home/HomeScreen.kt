package com.riadmahi.sample.home

import CustomButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(viewModel: HomeViewModel, onSignedOut: () -> Unit) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        if (uiState is HomeUiState.SignedOut) {
            onSignedOut()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🏠 Welcome")

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(
            modifier = Modifier.width(200.dp),
            text = "Sign out",
            onClick = {
                viewModel.signOut()
            }
        )

        if (uiState is HomeUiState.Error) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Erreur : ${uiState.message}",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}