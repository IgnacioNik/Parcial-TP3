package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.AccountBalanceScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.NotificationScreen
import com.example.myapplication.ui.screens.OnboardingScreen
import com.example.myapplication.ui.screens.RegisterScreen
import com.example.myapplication.ui.screens.SplashScreenContent
import com.example.myapplication.ui.screens.TransactionsScreen
import com.example.myapplication.ui.screens.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val view = LocalView.current
    val window = (view.context as? android.app.Activity)?.window

    LaunchedEffect(currentRoute) {
        if (window != null) {
            val isLight = when (currentRoute) {
                Screen.Welcome.route -> true
                else -> false
            }
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isLight
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {
            SplashScreenContent(
                onSplashFinished = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }


        composable(Screen.Onboarding.route) {
           OnboardingScreen(
                onNavigateToWelcome = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
                // (No más onGuestClick aquí)
            )
        }

        // --- 2. ACTUALIZA LOGIN SCREEN ---
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Navega a Home como USUARIO LOGUEADO
                    navController.navigate(Screen.Home.createRoute(isGuest = false)) {
                        popUpTo(navController.graph.startDestinationRoute!!) { inclusive = true }
                    }
                },
                onGuestLogin = {
                    // Navega a Home como INVITADO
                    navController.navigate(Screen.Home.createRoute(isGuest = true)) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onForgotPasswordClick = { /* ... */ },
                onSignUpClick = { /* ... */ },
                onFacebookprintClick = { /* ... */ },
                onGoogleClick = { /* ... */ },
                onBottomSignUpClick = { /* ... */ }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(

                onRegisterSuccess = {

                    navController.navigate(Screen.Login.route) {

                        popUpTo(navController.graph.startDestinationRoute!!) { inclusive = true }
                    }
                },
                onBackToLoginClick = {

                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onNavigateToNotification = { // <-- PÁSALO AQUÍ
                    navController.navigate(Screen.Notification.route)}
                )
        }

        composable(Screen.Notification.route) {
            NotificationScreen(navController = navController)
        }

        composable(Screen.AccountBalance.route){
            AccountBalanceScreen(navController = navController)
        }

        composable  (Screen.Transactions.route ){
             TransactionsScreen(navController = navController)
        }

    }

}
