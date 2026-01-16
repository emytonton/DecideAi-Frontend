package com.example.decideai_front.ui.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.decideai_front.R
import com.example.decideai_front.ui.components.AppBottomBar
import com.example.decideai_front.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    userToken: String,
    navController: NavHostController,
    onNavigateToSettings: () -> Unit,
    avatarUrl: String? = null
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "DecideAí",
                navController = navController,
                userToken = userToken,
                avatarUrl = avatarUrl,
                showBackButton = false,
                showProfileIcon = true
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userToken = userToken,
                userName = userName
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Olá $userName!", fontSize = 42.sp, fontWeight = FontWeight.Light)
            Text(text = "Pronto para tomar uma decisão?", color = Color.Gray, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(32.dp))

            DecisionCard(
                title = "Decisão em grupo",
                subtitle = "Crie uma votação e convide amigos para votar.",
                iconRes = R.drawable.icon_check,
                iconBgColor = Color(0xFF5B99E9),
                onClick = { navController.navigate("group_home/$userToken") }
            )

            DecisionCard(
                title = "Decisão solo",
                subtitle = "Escolha uma categoria e vamos escolher por você.",
                iconRes = R.drawable.icon_dice,
                iconBgColor = Color(0xFFC49AFA),
                onClick = { navController.navigate("solo_decision/$userName/$userToken") }
            )

            DecisionCard(
                title = "Decisão por opções",
                subtitle = "Crie uma lista com opções e vamos aleatoriamente escolher uma.",
                iconRes = R.drawable.icon_list,
                iconBgColor = Color(0xFF5EEAD4),
                onClick = { navController.navigate("my_lists/$userToken") }
            )
        }
    }
}

@Composable
fun DecisionCard(title: String, subtitle: String, iconRes: Int, iconBgColor: Color, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = iconBgColor
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            }

            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}