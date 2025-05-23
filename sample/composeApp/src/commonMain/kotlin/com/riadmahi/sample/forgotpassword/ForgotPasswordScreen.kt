package com.riadmahi.sample.forgotpassword

import CustomButton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riadmahi.sample.component.CustomSnackBar
import com.riadmahi.sample.component.TopBar

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    navigateBack: () -> Unit
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    val uiState by viewModel.uiState.collectAsState()
    val countdown by viewModel.countdown.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is ForgotPasswordUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as ForgotPasswordUiState.Error).message)
            }

            is ForgotPasswordUiState.Success -> {
                snackbarHostState.showSnackbar("Password reset link sent!")
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                CustomSnackBar(snackbarData)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = paddingValues.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            TopBar { navigateBack() }

            Text(
                text = "Reset your password 🔑",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Enter your email address below and we’ll send you a link to reset your password",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 12.dp),
                textAlign = TextAlign.Center
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = email,
                    onValueChange = { newValue -> email = newValue },
                    placeholder = { Text(text = "Email") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1E2F3),
                        unfocusedContainerColor = Color(0xFFF1E2F3),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = Color(0xFFBBA8BE),
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Email
                    ),
                    keyboardActions = KeyboardActions(),
                    maxLines = 1
                )
            }

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = if (countdown > 0) "Wait $countdown sec" else "Send a link",
                enabled = countdown == 0,
                onClick = { viewModel.sendPasswordReset(email.text) }
            )

            if (uiState is ForgotPasswordUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
