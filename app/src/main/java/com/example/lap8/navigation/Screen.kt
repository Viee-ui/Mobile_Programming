package com.example.lap8.navigation

sealed class Screen(val route: String) {
    object SignIn : Screen("signin")
    object SignUp : Screen("signup")
    object Home : Screen("home")
}