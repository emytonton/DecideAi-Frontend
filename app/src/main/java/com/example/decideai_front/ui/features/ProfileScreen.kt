package com.example.decideai_front.ui.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.decideai_front.ui.components.AppBottomBar
import com.example.decideai_front.ui.components.AppTopBar
import com.example.decideai_front.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userToken: String,
    viewModel: ProfileViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadProfile(userToken)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "DecideAí",
                navController = navController,
                userToken = userToken,
                showBackButton = false,
                showProfileIcon = false
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = "profile/$userToken",
                userToken = userToken
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Profile",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB3E5FC)),
                contentAlignment = Alignment.Center
            ) {
                if (!viewModel.avatar.isNullOrEmpty()) {
                    AsyncImage(
                        model = viewModel.avatar,
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Ícone padrão",
                        modifier = Modifier.size(90.dp),
                        tint = Color.White
                    )
                }
            }

            Text(
                text = "@${viewModel.username}",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = viewModel.email,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { navController.navigate("edit_profile/$userToken") },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91AFFF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Editar Perfil",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val sharedPrefs = navController.context.getSharedPreferences("user_prefs", 0)
                    sharedPrefs.edit().clear().apply()
                    navController.navigate("welcome") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Sair",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}