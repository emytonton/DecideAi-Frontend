package com.example.decideai_front.ui.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.decideai_front.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userToken: String,
    viewModel: ProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.loadProfile(userToken) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TopBar com botão voltar
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            Text("Profile", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 18.sp)
        }

        Spacer(Modifier.height(32.dp))

        // Avatar (Usando AsyncImage ou similar)
        Box(Modifier.size(120.dp).clip(CircleShape).background(Color(0xFFB3E5FC))) {
            // Se tiver imagem real, use Image/AsyncImage aqui
            Icon(Icons.Default.Person, null, Modifier.fillMaxSize().padding(20.dp), tint = Color.White)
        }

        Text("@${viewModel.username}", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 16.dp))
        Text(viewModel.email, color = Color.Gray)

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = { navController.navigate("edit_profile/$userToken") },
            modifier = Modifier.fillMaxWidth().height(56.dp).shadow(4.dp, RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9191FF)),
            shape = RoundedCornerShape(12.dp)
        ) { Text("Editar Perfil") }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { /* Lógica de Logout */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
            shape = RoundedCornerShape(12.dp)
        ) { Text("Sair") }
    }
}