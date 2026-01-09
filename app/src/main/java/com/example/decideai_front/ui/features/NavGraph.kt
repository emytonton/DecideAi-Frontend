package com.example.decideai_front.ui.features

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.decideai_front.ui.features.*
import com.example.decideai_front.ui.theme.DecideAiFrontTheme
import com.example.decideai_front.viewmodel.*
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun NavGraph(startToken: String?, startName: String?) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val themeViewModel: ThemeViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val friendsViewModel: FriendsViewModel = viewModel()
    val optionsViewModel: OptionsDecisionViewModel = viewModel()
    val groupViewModel: GroupDecisionViewModel = viewModel()

    val startRoute = if (startToken != null) "home/$startName/$startToken" else "welcome"

    DecideAiFrontTheme(darkTheme = themeViewModel.isDarkTheme) {
        NavHost(navController = navController, startDestination = startRoute) {

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

            composable("solo_decision/{userName}/{token}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                SoloDecisionScreen(
                    userName = name,
                    userToken = token,
                    navController = navController,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "decision_result/{title}/{details}/{userName}/{token}",
                arguments = listOf(
                    navArgument("title") { type = NavType.StringType },
                    navArgument("details") { type = NavType.StringType },
                    navArgument("userName") { type = NavType.StringType },
                    navArgument("token") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val title = URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", "UTF-8")
                val details = URLDecoder.decode(backStackEntry.arguments?.getString("details") ?: "", "UTF-8")
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""

                DecisionResultScreen(
                    title = title,
                    details = details,
                    userName = name,
                    userToken = token,
                    navController = navController,
                    onAccept = {
                        navController.navigate("home/$name/$token") {
                            popUpTo("home/$name/$token") { inclusive = true }
                        }
                    },
                    onReface = { navController.popBackStack() }
                )
            }

            composable("my_lists/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                MyListsScreen(
                    navController = navController,
                    token = token,
                    viewModel = optionsViewModel
                )
            }

            composable(
                route = "manage_list/{token}?listId={listId}",
                arguments = listOf(
                    navArgument("token") { type = NavType.StringType },
                    navArgument("listId") { type = NavType.StringType; nullable = true; defaultValue = null }
                )
            ) { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val listId = backStackEntry.arguments?.getString("listId")
                OptionsDecisionScreen(navController = navController, userToken = token, viewModel = optionsViewModel, listIdToEdit = listId)
            }

            composable("options_result/{resultText}") { backStackEntry ->
                val resultText = backStackEntry.arguments?.getString("resultText") ?: ""
                OptionsResultScreen(result = resultText, onClose = { navController.popBackStack() })
            }

            composable("profile/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                ProfileScreen(navController = navController, userToken = token, viewModel = profileViewModel, onNavigateBack = { navController.popBackStack() })
            }

            composable("settings/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                SettingsScreen(
                    token = token,
                    viewModel = profileViewModel,
                    themeViewModel = themeViewModel,
                    onNavigateToEditProfile = { navController.navigate("edit_profile/$token") },
                    onLogout = {
                        val sharedPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        sharedPrefs.edit().clear().apply()
                        loginViewModel.resetState()
                        profileViewModel.resetState()
                        navController.navigate("welcome") { popUpTo(0) { inclusive = true } }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("edit_profile/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                EditProfileScreen(navController = navController, userToken = token, viewModel = profileViewModel)
            }

            composable("friends/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                FriendsScreen(navController = navController, token = token, viewModel = friendsViewModel)
            }

            composable("search_users/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                SearchUsersScreen(navController = navController, token = token, viewModel = friendsViewModel)
            }

            composable("group_home/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                GroupHomeScreen(navController, token, groupViewModel)
            }

            composable("group_inbox/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                GroupInboxScreen(navController, token, groupViewModel)
            }
        }
    }
}