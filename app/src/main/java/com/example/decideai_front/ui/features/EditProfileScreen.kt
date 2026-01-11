package com.example.decideai_front.ui.features

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.decideai_front.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    userToken: String,
    viewModel: ProfileViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        if (viewModel.username.isEmpty() || viewModel.email.isEmpty()) {
            viewModel.loadProfile(userToken)
        }
    }


    // Launcher para abrir a galeria
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.updateAvatar(it.toString())
        }
    }

    LaunchedEffect(viewModel.updateSuccess) {
        if (viewModel.updateSuccess) navController.popBackStack()
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // Avatar editável
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFB3E5FC))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.avatarUrl != null) {
                AsyncImage(
                    model = viewModel.avatarUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            } else {
                Icon(Icons.Default.Person, null, Modifier.size(60.dp), tint = Color.White)
            }

            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }

        Text(
            "Editar avatar",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp).clickable { launcher.launch("image/*") }
        )

        Spacer(Modifier.height(24.dp))

        // Campo Username
        Text("Nome de usuário", modifier = Modifier.align(Alignment.Start), fontSize = 12.sp, color = Color.Gray)
        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.username = it },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.Edit, null) },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(16.dp))

        // Campo Email
        Text("E-mail", modifier = Modifier.align(Alignment.Start), fontSize = 12.sp, color = Color.Gray)
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.Edit, null) },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { viewModel.updateProfile(userToken) },
            enabled = !viewModel.isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp).shadow(4.dp, RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9191FF)),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (viewModel.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Salvar Perfil", color = Color.White)
        }
    }
}