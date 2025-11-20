package com.example.decideai_front.ui.features

import androidx.compose.foundation.Image
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
import com.example.decideai_front.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("DecideAí", fontSize = 18.sp) },
                actions = {
                    IconButton(onClick = { /* Perfil */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil", modifier = Modifier.size(32.dp))
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(painterResource(id = R.drawable.ic_person_add), contentDescription = null) },
                    selected = false, onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(painterResource(id = R.drawable.ic_home), contentDescription = "Início") },
                    selected = true, onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(painterResource(id = R.drawable.ic_settings), contentDescription = null) },
                    selected = false, onClick = {}
                )
            }
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

            Text(text = "Olá Anna!", fontSize = 42.sp, fontWeight = FontWeight.Light)
            Text(text = "Pronto para tomar uma decisão?", color = Color.Gray, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(32.dp))

            // Cartões de Decisão
            DecisionCard(
                title = "Decisão em grupo",
                subtitle = "Crie uma votação e convide amigos para votar.",
                iconRes = R.drawable.ic_group_decision,
                iconBgColor = Color(0xFF5B99E9)
            )

            DecisionCard(
                title = "Decisão solo",
                subtitle = "Escolha uma categoria e vamos escolher por você.",
                iconRes = R.drawable.ic_solo_decision,
                iconBgColor = Color(0xFFC49AFA)
            )

            DecisionCard(
                title = "Decisão por opções",
                subtitle = "Crie uma lista com opções e vamos aleatoriamente escolher uma.",
                iconRes = R.drawable.ic_options_decision,
                iconBgColor = Color(0xFF5EEAD4)
            )
        }
    }
}

@Composable
fun DecisionCard(title: String, subtitle: String, iconRes: Int, iconBgColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
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

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = subtitle, color = Color.Gray, fontSize = 14.sp, lineHeight = 18.sp)
            }

            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Black)
        }
    }
}