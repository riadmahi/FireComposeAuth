package com.riadmahi.sample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.riadmahi.firecomposeauth.FireComposeAuth
import com.riadmahi.sample.login.LoginScreen
import com.riadmahi.sample.login.LoginViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    auth: FireComposeAuth
) {
    val userIsLoggedIn = auth.currentUser()

    NavHost(
        navController = navController,
        startDestination = if (userIsLoggedIn == null) AppDestination.Login.route else AppDestination.Home.route, // "", //3017620422003
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(route = AppDestination.Login.route) {
            val viewModel = remember { LoginViewModel(auth) }
            LoginScreen(
                viewModel = viewModel,
                navigateToForgotPassword = { navController.navigate(AppDestination.ForgotPassword.route) },
                navigateToHome = {
                    navController.navigate(AppDestination.Home.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

sealed class AppDestination(val route: String) {
    data object Login : AppDestination(route = "login")
    data object ForgotPassword : AppDestination(route = "forgot-password")
    data object Register : AppDestination(route = "register")
    data object Home : AppDestination(route = "home")
}
