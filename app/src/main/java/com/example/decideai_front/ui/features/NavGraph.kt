package com.example.decideai_front.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decideai_front.ui.features.*
import com.example.decideai_front.viewmodel.FriendsViewModel
import com.example.decideai_front.viewmodel.LoginViewModel
import com.example.decideai_front.viewmodel.ProfileViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()


    val loginViewModel: LoginViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()


    val friendsViewModel: FriendsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {

        composable("welcome") {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }


        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToHome = { name, token ->
                    navController.navigate("home/$name/$token") {

                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }


        composable("register") {
            RegisterScreen(onNavigateToLogin = { navController.navigate("login") })
        }


        composable("home/{userName}/{token}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
            val token = backStackEntry.arguments?.getString("token") ?: ""

            HomeScreen(
                userName = name,
                navController = navController,
                userToken = token,
                onNavigateToSettings = { navController.navigate("settings/$token") }
            )
        }


        composable("solo_decision/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            SoloDecisionScreen(
                onNavigateBack = { navController.popBackStack() },
                userToken = token,
                navController = navController
            )
        }

        composable("decision_result/{title}/{details}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "Ops"
            val details = backStackEntry.arguments?.getString("details") ?: ""
            DecisionResultScreen(
                title = title,
                details = details,
                onAccept = { navController.popBackStack("home", inclusive = false) },
                onReface = { navController.popBackStack() }
            )
        }

        composable("options_decision/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            OptionsDecisionScreen(
                navController = navController,
                userToken = token,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("options_result/{resultText}") { backStackEntry ->
            val resultText = backStackEntry.arguments?.getString("resultText") ?: ""
            OptionsResultScreen(
                result = resultText,
                onClose = { navController.popBackStack() }
            )
        }


        composable("profile/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            ProfileScreen(
                navController = navController,
                userToken = token,
                viewModel = profileViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable("settings/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""

            SettingsScreen(
                token = token,
                viewModel = profileViewModel,
                onNavigateToEditProfile = {
                    navController.navigate("edit_profile/$token")
                },
                onLogout = {
                    loginViewModel.resetState()
                    profileViewModel.resetState()
                    navController.navigate("welcome") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable("edit_profile/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            EditProfileScreen(
                navController = navController,
                userToken = token,
                viewModel = profileViewModel
            )
        }


        composable("friends/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            FriendsScreen(
                navController = navController,
                token = token,
                viewModel = friendsViewModel
            )
        }


        composable("search_users/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            SearchUsersScreen(
                navController = navController,
                token = token,
                viewModel = friendsViewModel
            )
        }
    }
}