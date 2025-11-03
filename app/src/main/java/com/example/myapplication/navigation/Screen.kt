package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    data object Home : Screen("home/{isGuest}") {
        // Función "ayudante" para crear la ruta
        fun createRoute(isGuest: Boolean): String = "home/$isGuest"
    }
    object Transactions : Screen("transactions")
    object Notification : Screen("notification")
    object Profile : Screen("profile")
    object AccountBalance : Screen("account_balance")
    object Categories : Screen("categories")
}