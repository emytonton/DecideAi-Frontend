package com.example.decideai_front.ui

import androidx.compose.runtime.Composable
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
fun NavGraph() {
    val navController = rememberNavController()

    // --- INSTÂNCIA DOS VIEWMODELS ---
    val themeViewModel: ThemeViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val friendsViewModel: FriendsViewModel = viewModel()
    val optionsViewModel: OptionsDecisionViewModel = viewModel()
    val groupViewModel: GroupDecisionViewModel = viewModel()

    // Aplica o tema (Dark/Light) controlado pelo ThemeViewModel
    DecideAiFrontTheme(darkTheme = themeViewModel.isDarkTheme) {
        NavHost(
            navController = navController,
            startDestination = "welcome"
        ) {
            // --- TELA INICIAL ---
            composable("welcome") {
                WelcomeScreen(
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }

            // --- LOGIN ---
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

            // --- CADASTRO ---
            composable("register") {
                RegisterScreen(onNavigateToLogin = { navController.navigate("login") })
            }

            // --- HOME ---
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

            // --- DECISÃO SOLO ---
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
                    onAccept = {
                        // Volta para a Home limpando a pilha de decisão
                        navController.navigate("home/Usuário/${loginViewModel.userToken}") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onReface = { navController.popBackStack() }
                )
            }

            // --- MINHAS LISTAS (OPÇÕES) ---
            composable("my_lists/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                MyListsScreen(
                    navController = navController,
                    token = token,
                    viewModel = optionsViewModel
                )
            }

            // CRIAR/EDITAR LISTA
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

            // --- PERFIL E CONFIGURAÇÕES ---
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

            // --- AMIGOS (SOCIAL) ---
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

            // --- DECISÃO EM GRUPO (NOVO) ---

            // 1. Home do Grupo (Tela Inicial do fluxo)
            composable("group_home/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                GroupHomeScreen(navController, token, groupViewModel)
            }

            // 2. Selecionar Amigos (Passo 2 da criação)
            composable(
                route = "select_friends/{token}?title={title}&options={options}",
                arguments = listOf(
                    navArgument("token") { type = NavType.StringType },
                    navArgument("title") { type = NavType.StringType; defaultValue = "" },
                    navArgument("options") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val optionsStr = backStackEntry.arguments?.getString("options") ?: ""

                SelectFriendsScreen(navController, token, title, optionsStr, groupViewModel)
            }

            // 3. Inbox (Lista de decisões em andamento)
            composable("group_inbox/{token}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                GroupInboxScreen(navController, token, groupViewModel)
            }

            // 4. Votação
            composable("vote_group/{token}/{id}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val id = backStackEntry.arguments?.getString("id") ?: ""
                VoteGroupScreen(navController, token, id, groupViewModel)
            }

            // 5. Resultado Final
            composable("group_result/{token}/{id}") { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val id = backStackEntry.arguments?.getString("id") ?: ""
                GroupResultScreen(navController, token, id, groupViewModel)
            }
        }
    }
}