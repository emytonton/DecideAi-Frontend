package com.example.decideai_front.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decideai_front.ui.features.LoginScreen
import com.example.decideai_front.ui.features.RegisterScreen
import com.example.decideai_front.ui.features.WelcomeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome" // Define a tela inicial
    ) {
        // Rota da Tela Inicial
        composable("welcome") {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // Rota da Tela de Login
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // Rota da Tela de Cadastro
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
    }
}