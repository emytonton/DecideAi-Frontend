package com.example.decideai_front.ui.features

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.decideai_front.R
import com.example.decideai_front.ui.components.AppBottomBar
import com.example.decideai_front.viewmodel.GroupDecisionViewModel

val BluePrimary = Color(0xFF6E9CE6)
val BlueCardResult = Color(0xFF8AB4F8)
val RedRemove = Color(0xFFE57373)
val InputGray = Color(0xFFF5F5F5)
val GrayText = Color(0xFF757575)

@Composable
fun ErrorDisplay(message: String?, onRetry: () -> Unit) {
    if (message != null) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Warning, contentDescription = "Erro", tint = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = message, color = Color.Red, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = RedRemove)
                ) {
                    Text("Tentar Novamente")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupHomeScreen(
    navController: NavController,
    token: String,
    viewModel: GroupDecisionViewModel
) {
    var title by remember { mutableStateOf("") }
    var currentOption by remember { mutableStateOf("") }
    val optionsList = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Decisão em grupo", fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userToken = token
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
        ) {

            ErrorDisplay(message = viewModel.errorMessage, onRetry = { viewModel.errorMessage = null })

            Text(
                "Decida em grupo!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text("Decisão à tomar:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            GroupTextField(value = title, onValueChange = { title = it }, placeholder = "O que iremos assistir?")

            Spacer(modifier = Modifier.height(16.dp))

            Text("Opções:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                GroupTextField(
                    value = currentOption,
                    onValueChange = { currentOption = it },
                    placeholder = "Ação...",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (currentOption.isNotBlank()) {
                            optionsList.add(currentOption)
                            currentOption = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(BluePrimary, RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = optionsList) { opt ->
                    OptionItemDisplay(text = opt, onRemove = { optionsList.remove(opt) })
                }
            }

            Button(
                onClick = {
                    navController.navigate("select_friends/$token?title=$title&options=${optionsList.joinToString(",")}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.icon_dice),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chame seus amigos", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("group_inbox/$token") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Decisões em andamento", fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFriendsScreen(
    navController: NavController,
    token: String,
    title: String,
    optionsString: String,
    viewModel: GroupDecisionViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadFriendsForSelection(token)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Convide seus amigos", fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userToken = token
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
        ) {

            ErrorDisplay(message = viewModel.errorMessage, onRetry = { viewModel.loadFriendsForSelection(token) })

            GroupTextField(value = searchQuery, onValueChange = { searchQuery = it }, placeholder = "Digite o nome..")
            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items = viewModel.friendsToInvite) { friend ->
                    if (friend.username.contains(searchQuery, ignoreCase = true)) {
                        val isSelected = viewModel.selectedFriendIds.contains(friend.id)
                        FriendSelectionItem(
                            name = friend.username,
                            avatarUrl = friend.avatar,
                            isSelected = isSelected,
                            onToggle = { viewModel.toggleFriendSelection(friend.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val optionsList = optionsString.split(",").filter { it.isNotBlank() }
                    viewModel.createDecision(token, title, optionsList) {
                        navController.navigate("group_inbox/$token") {
                            popUpTo("group_home") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(12.dp),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Confirmar Convites")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupInboxScreen(
    navController: NavController,
    token: String,
    viewModel: GroupDecisionViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadDecisions(token)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Minhas Decisões", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userToken = token
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
        ) {

            ErrorDisplay(message = viewModel.errorMessage, onRetry = { viewModel.loadDecisions(token) })

            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    if (viewModel.pendingDecisions.isNotEmpty()) {
                        item { GroupHeader("Esperando por você ⏳") }
                        items(items = viewModel.pendingDecisions) { decision ->
                            InboxItem(
                                title = decision.title,
                                statusLabel = "Pendente",
                                actionLabel = "Votar",
                                showRemove = true,
                                onAction = { navController.navigate("vote_group/$token/${decision.id}") },
                                onRemove = { viewModel.decline(token, decision.id) }
                            )
                        }
                    }

                    if (viewModel.inProgressDecisions.isNotEmpty()) {
                        item { GroupHeader("Em andamento 🎲") }
                        items(items = viewModel.inProgressDecisions) { decision ->
                            InboxItem(
                                title = decision.title,
                                statusLabel = "Aguardando votos",
                                actionLabel = "Ver status",
                                showRemove = false,
                                onAction = {
                                    Toast.makeText(context, "Aguardando todos votarem...", Toast.LENGTH_SHORT).show()
                                },
                                onRemove = {}
                            )
                        }
                    }

                    if (viewModel.finishedDecisions.isNotEmpty()) {
                        item { GroupHeader("Decisões Tomadas ✅") }
                        items(items = viewModel.finishedDecisions) { decision ->
                            InboxItem(
                                title = decision.title,
                                statusLabel = "Vencedor: ${decision.winner}",
                                actionLabel = "Ver Resultado",
                                showRemove = false,
                                isFinished = true,
                                hasViewed = decision.hasViewedResult ?: true,
                                onAction = { navController.navigate("group_result/$token/${decision.id}") },
                                onRemove = {}
                            )
                        }
                    }

                    if (viewModel.groupDecisions.isEmpty()) {
                        item {
                            Text(
                                "Nenhuma decisão encontrada.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp),
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteGroupScreen(
    navController: NavController,
    token: String,
    decisionId: String,
    viewModel: GroupDecisionViewModel
) {
    LaunchedEffect(decisionId) {
        viewModel.loadDecisionDetails(token, decisionId)
    }

    val decision = viewModel.currentDecision
    var selectedOption by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Votação", fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userToken = token
            )
        }
    ) { padding ->
        if (viewModel.errorMessage != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                ErrorDisplay(
                    message = viewModel.errorMessage,
                    onRetry = { viewModel.loadDecisionDetails(token, decisionId) }
                )
            }
        } else if (viewModel.isLoading || decision == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                Text(decision.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Selecione a opção que preferir.", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(items = decision.options) { option ->
                        val isSelected = selectedOption == option
                        VoteOptionItem(
                            text = option,
                            isSelected = isSelected,
                            onClick = { selectedOption = option }
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.vote(token, decisionId, selectedOption) { isFinished ->
                            if (isFinished) {
                                navController.navigate("group_result/$token/$decisionId") {
                                    popUpTo("group_inbox/$token") { inclusive = false }
                                }
                            } else {
                                navController.navigate("group_inbox/$token") {
                                    popUpTo("group_home/$token") { inclusive = false }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    shape = RoundedCornerShape(12.dp),
                    enabled = selectedOption.isNotEmpty() && !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text("Votar!")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupResultScreen(
    navController: NavController,
    token: String,
    decisionId: String,
    viewModel: GroupDecisionViewModel
) {
    LaunchedEffect(decisionId) {
        viewModel.loadDecisionDetails(token, decisionId)
        viewModel.markAsViewed(token, decisionId)
    }

    val decision = viewModel.currentDecision

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Resultado", fontSize = 16.sp) })
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userToken = token
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.errorMessage != null) {
                ErrorDisplay(
                    message = viewModel.errorMessage,
                    onRetry = { viewModel.loadDecisionDetails(token, decisionId) }
                )
            }

            Text("Decisão final!", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Text(decision?.winner ?: "Carregando...", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            HorizontalDivider(
                modifier = Modifier
                    .width(60.dp)
                    .padding(vertical = 8.dp),
                thickness = 2.dp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BlueCardResult)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Vencedor", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(decision?.winner ?: "-", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    navController.navigate("home/Usuário/$token") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text("Ir para Home")
            }
        }
    }
}

@Composable
fun GroupHeader(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = BluePrimary,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun GroupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.LightGray) },
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun OptionItemDisplay(text: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(id = R.drawable.icon_dice),
            contentDescription = null,
            tint = BluePrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, modifier = Modifier.weight(1f), color = Color.Gray)
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .size(24.dp)
                .background(RedRemove, CircleShape)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remover",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun FriendSelectionItem(name: String, avatarUrl: String?, isSelected: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray))
        Spacer(modifier = Modifier.width(12.dp))
        Text(name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(if (isSelected) BluePrimary else InputGray, RoundedCornerShape(4.dp))
                .clickable { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun InboxItem(
    title: String,
    statusLabel: String,
    actionLabel: String,
    showRemove: Boolean,
    onAction: () -> Unit,
    onRemove: () -> Unit,
    isFinished: Boolean = false,
    hasViewed: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(statusLabel, fontSize = 12.sp, color = if (isFinished) BluePrimary else GrayText)
        }

        if (isFinished && !hasViewed) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Red))
            Spacer(modifier = Modifier.width(8.dp))
        }

        Button(
            onClick = onAction,
            colors = ButtonDefaults.buttonColors(containerColor = if (isFinished) Color(0xFF4CAF50) else BluePrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(actionLabel, fontSize = 12.sp)
        }

        if (showRemove) {
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .size(24.dp)
                    .background(RedRemove, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "X", tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
fun VoteOptionItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isSelected) 0.dp else 2.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(id = R.drawable.icon_dice),
            contentDescription = null,
            tint = BluePrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isSelected) BluePrimary else InputGray),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}
