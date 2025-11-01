package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Welcome : Screen("welcome") // La que era LaunchScreenB
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Transactions : Screen("transactions")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
}