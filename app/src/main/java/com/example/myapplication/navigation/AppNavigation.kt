package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import com.example.myapplication.ui.screens.LoginScreen // Importa tus pantallas
import com.example.myapplication.ui.screens.OnboardingScreen
//import com.example.myapplication.ui.screens.RegisterScreen
import com.example.myapplication.ui.screens.SplashScreenContent
import com.example.myapplication.ui.screens.WelcomeScreen // Asumiré que renombraste tu Composable de LaunchScreenB

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route // Empezamos por el Splash
    ) {
        // Ruta para el Splash Screen
        composable(Screen.Splash.route) {
            SplashScreenContent(
                onSplashFinished = {
                    // Cuando el splash termine, navega a Onboarding
                    navController.navigate(Screen.Onboarding.route) {
                        // Limpia el stack para que el usuario no pueda "volver" al splash
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Ruta para el Onboarding
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToWelcome = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Ruta para la pantalla de Bienvenida (ex LaunchScreenB)
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        // Ruta para el Login
//        composable(Screen.Login.route) {
//            // Asumiendo que tu LoginScreen tiene un lambda para volver atrás
//            LoginScreen(
//                onBackClick = { navController.popBackStack() }
//                // ...otros lambdas para onLoginSuccess, etc.
//            )
//        }
//
//        // Ruta para el Registro
//        composable(Screen.Register.route) {
//            RegisterScreen(
//                onBackClick = { navController.popBackStack() }
//                // ...etc.
//            )
//        }

        // ... (Añade el resto de tus rutas aquí: Home, Notifications, etc.)
    }
}