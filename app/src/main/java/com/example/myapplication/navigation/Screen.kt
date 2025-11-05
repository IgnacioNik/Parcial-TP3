package com.example.myapplication.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen(route = "home/{isGuest}") {
        // 1. Define la lista de argumentos que esta ruta espera
        val arguments = listOf(
            navArgument("isGuest") {
                type = NavType.BoolType // El tipo de dato es Booleano
            }
        )
        // 2. Función ayudante para crear la ruta (ej: "home/true" o "home/false")
        fun createRoute(isGuest: Boolean): String {
            return "home/$isGuest"
        }
    }
    object Transactions : Screen("transactions")
    object Notification : Screen("notification")
    object Profile : Screen("profile")
    object AccountBalance : Screen("account_balance")
    object Categories : Screen("categories")
}