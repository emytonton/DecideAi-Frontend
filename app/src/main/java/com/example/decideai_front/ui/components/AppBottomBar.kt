package com.example.decideai_front.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.decideai_front.R

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String?,
    userToken: String
) {
    Column {
        // Linha divisória fina inferior
        HorizontalDivider(
            thickness = 0.8.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
        )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp // Removido para manter a cor sólida #2E236C
        ) {
            NavigationBarItem(
                icon = { Icon(painterResource(R.drawable.icon_add_person), null, Modifier.size(28.dp)) },
                selected = currentRoute == "friends/$userToken",
                onClick = { navController.navigate("friends/$userToken") }
            )
            NavigationBarItem(
                icon = { Icon(painterResource(R.drawable.icon_home), "Início", Modifier.size(28.dp)) },
                selected = currentRoute?.contains("home") == true,
                onClick = { navController.navigate("home/user/$userToken") }
            )
            NavigationBarItem(
                icon = { Icon(painterResource(R.drawable.icon_settings), "Configurações", Modifier.size(28.dp)) },
                selected = currentRoute == "settings/$userToken",
                onClick = { navController.navigate("settings/$userToken") }
            )
        }
    }
}