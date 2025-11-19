package com.example.decideai_front.ui.features

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.decideai_front.R
import com.example.decideai_front.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel()
) {
    // Observa o sucesso para mudar de tela automaticamente
    LaunchedEffect(viewModel.isSuccess) {
        if (viewModel.isSuccess) {
            onNavigateToLogin()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()), // Garante que caiba em telas menores
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Use o nome do seu arquivo
            contentDescription = "Logo",
            modifier = Modifier.size(160.dp)
        )

        Text(
            text = "Vamos começar",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black
        )
        Text(
            text = "Sua indecisão acaba em uma rodada",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo Email
        CustomTextField(value = viewModel.email, onValueChange = { viewModel.email = it }, placeholder = "Email")

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Username (Exigido pela API)
        CustomTextField(value = viewModel.username, onValueChange = { viewModel.username = it }, placeholder = "Username")

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Senha
        CustomTextField(value = viewModel.password, onValueChange = { viewModel.password = it }, placeholder = "Senha", isPassword = true)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Confirmação de Senha
        CustomTextField(value = viewModel.confirmPassword, onValueChange = { viewModel.confirmPassword = it }, placeholder = "Confirme sua Senha", isPassword = true)

        Spacer(modifier = Modifier.height(32.dp))

        // Botão Cadastrar (Azul do Figma)
        Button(
            onClick = { viewModel.onRegisterClick() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF324290)),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Cadastrar", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text("Já tem uma conta? ", color = Color.Gray)
            Text(
                text = "Logar",
                color = Color(0xFF324290),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }

        viewModel.errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

// Componente reutilizável para os campos de texto do Figma
@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, placeholder: String, isPassword: Boolean = false) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFD9D9D9),
            unfocusedContainerColor = Color(0xFFD9D9D9),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        )
    )
}