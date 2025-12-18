package com.example.decideai_front.ui.features

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decideai_front.viewmodel.ProfileViewModel
import com.example.decideai_front.viewmodel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    token: String,
    viewModel: ProfileViewModel,
    themeViewModel: ThemeViewModel,
    onNavigateToEditProfile: () -> Unit,
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current


    var isDarkTheme by remember { mutableStateOf(false) }
    var areNotificationsEnabled by remember { mutableStateOf(false) }


    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Configurações", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "Aparências",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsOptionItem(
                label = "Tema Escuro",
                icon = Icons.Default.Palette,
                isChecked = themeViewModel.isDarkTheme,
                onCheckedChange = { themeViewModel.toggleTheme() }
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Notificações",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsOptionItem(
                label = "Ativar notificações",
                icon = Icons.Default.Notifications,
                isChecked = areNotificationsEnabled,
                onCheckedChange = { areNotificationsEnabled = it }
            )

            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = onNavigateToEditProfile,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)), // Azul índigo
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Editar Perfil", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))


            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5)), // Cinza claro
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sair", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))


            Button(
                onClick = { showDeleteDialog = true }, // Abre o Modal
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)), // Vermelho suave
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Apagar conta", color = Color.White, fontSize = 16.sp)
            }
        }
    }


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(text = "Excluir conta?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Essa ação é irreversível. Você perderá o acesso e seus dados serão ocultados. Deseja continuar?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false

                        viewModel.deleteAccount(
                            token = token,
                            onSuccess = {
                                Toast.makeText(context, "Conta excluída com sucesso.", Toast.LENGTH_SHORT).show()
                                onLogout()
                            },
                            onError = { erro ->
                                Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Sim, excluir")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}


@Composable
fun SettingsOptionItem(
    label: String,
    icon: ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(12.dp)) // Fundo cinza claro
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, fontWeight = FontWeight.Medium, color = Color.Black)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color.Black,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}