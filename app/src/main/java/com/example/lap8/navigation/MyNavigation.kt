package com.example.lap8.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lap8.ui.screen.HomeScreen

import com.example.lap8.ui.screen.SignInScreen
import com.example.lap8.ui.screen.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyNavigation() {
    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser

    val startDestination = if (currentUser != null) {
        Screen.Home.route
    } else {
        Screen.SignIn.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
    }
}