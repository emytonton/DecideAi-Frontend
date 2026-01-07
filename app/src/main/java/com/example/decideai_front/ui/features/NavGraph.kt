package com.example.decideai_front.ui

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

@Composable
fun NavGraph(startToken: String?, startName: String?) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val themeViewModel: ThemeViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val friendsViewModel: FriendsViewModel = viewModel()
    val optionsViewModel: OptionsDecisionViewModel = viewModel()
    val groupDecisionViewModel: GroupDecisionViewModel = viewModel()

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

            composable("create_group_decision/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                CreateGroupDecisionScreen(
                    groupVm = groupDecisionViewModel,
                    userToken = token,
                    onNavigateToInvite = { navController.navigate("invite_friends/$token") },
                    onNavigateToInbox = { navController.navigate("group_inbox/$token") }
                )
            }

            composable("invite_friends/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                InviteFriendsScreen(
                    friendsVm = friendsViewModel,
                    groupVm = groupDecisionViewModel,
                    token = token,
                    onSuccess = {
                        navController.popBackStack("home/{userName}/{token}", inclusive = false)
                    }
                )
            }

            composable("group_inbox/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                GroupInboxScreen(
                    groupVm = groupDecisionViewModel,
                    token = token,
                    onNavigateToVote = { id -> navController.navigate("group_voting/$token/$id") }
                )
            }

            composable("group_voting/{token}/{decisionId}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val decisionId = backStackEntry.arguments?.getString("decisionId") ?: ""
                VotingScreen(
                    decisionId = decisionId,
                    groupVm = groupDecisionViewModel,
                    token = token,
                    onNavigateToResult = { winner ->
                        navController.navigate("group_result/$token/$decisionId/$winner")
                    }
                )
            }

            composable("group_result/{token}/{decisionId}/{winner}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val decisionId = backStackEntry.arguments?.getString("decisionId") ?: ""
                val winner = backStackEntry.arguments?.getString("winner") ?: ""
                GroupResultScreen(
                    winner = winner,
                    decisionId = decisionId,
                    groupVm = groupDecisionViewModel,
                    token = token,
                    onClose = {
                        navController.popBackStack("home/{userName}/{token}", inclusive = false)
                    }
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
                    onAccept = { navController.popBackStack("home/{userName}/{token}", inclusive = false) },
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
                    navArgument("listId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val listId = backStackEntry.arguments?.getString("listId")

                OptionsDecisionScreen(
                    navController = navController,
                    userToken = token,
                    viewModel = optionsViewModel,
                    listIdToEdit = listId
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
                    themeViewModel = themeViewModel,
                    onNavigateToEditProfile = { navController.navigate("edit_profile/$token") },
                    onLogout = {
                        val sharedPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        sharedPrefs.edit().clear().apply()

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
}