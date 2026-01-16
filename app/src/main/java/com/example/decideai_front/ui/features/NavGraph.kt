package com.example.decideai_front.ui.features

import android.R.attr.name
import android.content.Context
import android.os.Bundle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.decideai_front.data.DataStorageManager
import com.example.decideai_front.ui.features.*
import com.example.decideai_front.ui.theme.DecideAiFrontTheme
import com.example.decideai_front.viewmodel.*
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun NavGraph(startToken: String?, startName: String?, initialDarkMode: Boolean) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val themeViewModel: ThemeViewModel = viewModel()
    val isDark = themeViewModel.isDarkTheme
    val loginViewModel: LoginViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val friendsViewModel: FriendsViewModel = viewModel()
    val optionsViewModel: OptionsDecisionViewModel = viewModel()
    val groupViewModel: GroupDecisionViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val dataStorageManager = DataStorageManager(context)

    val startRoute = if (!startToken.isNullOrEmpty()) {
        "home/$startName/$startToken"
    } else {
        "welcome"
    }

    DecideAiFrontTheme(darkTheme = isDark) {
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

                val nameFromRoute = backStackEntry.arguments?.getString("userName")
                val name = if (!nameFromRoute.isNullOrEmpty() && nameFromRoute != "{userName}") {
                    nameFromRoute
                } else {
                    startName ?: "Usuário"
                }

                val token = backStackEntry.arguments?.getString("token") ?: ""

                LaunchedEffect(Unit) {
                    profileViewModel.loadProfile(token)
                }
                HomeScreen(
                    userName = name,
                    navController = navController,
                    userToken = token,
                    avatarUrl = profileViewModel.avatar,
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
                    onNavigateBack = { navController.popBackStack() },
                    avatarUrl = profileViewModel.avatar
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
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                MyListsScreen(
                    navController = navController,
                    token = token,
                    viewModel = optionsViewModel,
                    avatarUrl = profileViewModel.avatar,
                    userName = name
                )
            }

            composable(
                route = "manage_list/{token}?listId={listId}",
                arguments = listOf(
                    navArgument("token") { type = NavType.StringType },
                    navArgument("listId") { type = NavType.StringType; nullable = true; defaultValue = null }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val listId = backStackEntry.arguments?.getString("listId")
                OptionsDecisionScreen(
                    navController = navController,
                    userToken = token,
                    viewModel = optionsViewModel,
                    avatarUrl = profileViewModel.avatar,
                    listIdToEdit = listId,
                    userName = name
                )
            }

            composable("options_result/{resultText}") { backStackEntry ->
                val resultText = backStackEntry.arguments?.getString("resultText") ?: ""
                OptionsResultScreen(result = resultText, onClose = { navController.popBackStack() })
            }

            composable("profile/{userToken}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("userToken") ?: ""
                ProfileScreen(navController = navController, userToken = token, userName = name)
            }

            composable("edit_profile/{userToken}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("userToken") ?: ""
                EditProfileScreen(navController = navController, userToken = token, userName = name)
            }


            composable("settings/{userName}/{token}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                SettingsScreen(
                    navController = navController,
                    token = token,
                    viewModel = profileViewModel,
                    themeViewModel = themeViewModel,
                    onNavigateToEditProfile = { navController.navigate("edit_profile/$token") },
                    onLogout = {
                        scope.launch {
                            dataStorageManager.saveSession("", "")
                            loginViewModel.resetState()
                            profileViewModel.resetState()
                            navController.navigate("welcome") {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                    userName = name
                )
            }

            composable("friends/{userName}/{token}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                FriendsScreen(
                    navController = navController,
                    token = token,
                    userName = name,
                    viewModel = friendsViewModel)
            }

            composable("search_users/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                SearchUsersScreen(navController = navController, token = token, viewModel = friendsViewModel)
            }

            composable("group_home/{token}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                GroupHomeScreen(
                    navController,
                    token,
                    groupViewModel,
                    avatarUrl =  profileViewModel.avatar,
                    userName = name
                )
            }

            composable("group_inbox/{token}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                GroupInboxScreen(navController, token, groupViewModel, userName = name)
            }

            composable(
                route = "select_friends/{token}?title={title}&options={options}",
                arguments = listOf(
                    navArgument("token") { type = NavType.StringType },
                    navArgument("title") { type = NavType.StringType; defaultValue = "" },
                    navArgument("options") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val title = URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", "UTF-8")
                val options = URLDecoder.decode(backStackEntry.arguments?.getString("options") ?: "", "UTF-8")
                SelectFriendsScreen(navController, token, title, options, groupViewModel, userName = name)
            }
            composable(
                route = "vote_group/{token}/{decisionId}",
                arguments = listOf(
                    navArgument("token") { type = NavType.StringType },
                    navArgument("decisionId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val decisionId = backStackEntry.arguments?.getString("decisionId") ?: ""

                VoteGroupScreen(
                    navController = navController,
                    token = token,
                    decisionId = decisionId,
                    viewModel = groupViewModel,
                    userName = name
                )
            }

            composable(
                route = "group_result/{token}/{decisionId}",
                arguments = listOf(
                    navArgument("token") { type = NavType.StringType },
                    navArgument("decisionId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: "Usuário"
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val decisionId = backStackEntry.arguments?.getString("decisionId") ?: ""

                GroupResultScreen(
                    navController = navController,
                    token = token,
                    decisionId = decisionId,
                    onClose = { navController.popBackStack() },
                    viewModel = groupViewModel,
                    userName = name
                )
            }

        }
    }
}