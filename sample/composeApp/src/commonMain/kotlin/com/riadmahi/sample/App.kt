package com.riadmahi.sample

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.riadmahi.firecomposeauth.getFireComposeAuth
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val context = getPlatformContext()
    val auth = getFireComposeAuth(context)
    val navController = rememberNavController()
    MaterialTheme {
        AppNavHost(navController = navController, auth = auth)
    }
}