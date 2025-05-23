package com.riadmahi.sample.login

import CustomButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riadmahi.sample.component.CustomSnackBar
import com.riadmahi.sample.component.PasswordTextField

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToForgotPassword: () -> Unit,
    navigateToHome: () -> Unit
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Error -> {
                snackbarHostState.showSnackbar(
                    message = (uiState as LoginUiState.Error).message,
                    actionLabel = "Dismiss"
                )
            }

            is LoginUiState.Success -> {
                navigateToHome()
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .padding(top = 64.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(48.dp)
            ) {

                Text(
                    text = "Welcome back 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Medium
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextField(
                        value = email,
                        onValueChange = { email = it },
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
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        )
                    )

                    PasswordTextField(
                        password = password.text,
                        onPasswordChange = { password = TextFieldValue(it) }
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Forgot your password?",
                            fontSize = 14.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { navigateToForgotPassword() }
                        )
                    }
                }

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Sign in",
                    onClick = { viewModel.login(email.text, password.text) }
                )
            }

            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}