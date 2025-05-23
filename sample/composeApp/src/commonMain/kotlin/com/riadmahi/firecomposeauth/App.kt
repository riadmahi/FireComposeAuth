package com.riadmahi.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.riadmahi.firecomposeauth.AuthResult
import com.riadmahi.firecomposeauth.getFireComposeAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val context = getPlatformContext()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }
    val auth = getFireComposeAuth(getPlatformContext())
    MaterialTheme {
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

                Button(onClick = {
                    result = null
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val resultValue = auth.login(email, password)
                            result = when (resultValue) {
                                is AuthResult.Success -> "✅ Welcome ${resultValue.user.email}"
                                is AuthResult.Error -> "❌ ${resultValue.message}"
                            }
                        } catch (e: Exception) {
                            result = "❌ ${e.message}"
                        }
                    }
                }) {
                    Text("Login")
                }

                result?.let {
                    Text(it)
                }
            }
    }
}