package com.example.decideai_front.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.RegisterRequest
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var email by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var showErrorDialog by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)

    fun onRegisterClick() {

        if (email.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Todos os campos são obrigatórios. Por favor, preencha tudo."
            showErrorDialog = true
            return
        }


        if (password != confirmPassword) {
            errorMessage = "As senhas não coincidem. Verifique e tente novamente."
            showErrorDialog = true
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val request = RegisterRequest(username, email, password)
                val response = RetrofitClient.service.signup(request)

                if (response.isSuccessful) {
                    isSuccess = true
                } else {
                    errorMessage = when (response.code()) {
                        409 -> "Este e-mail ou nome de usuário já está em uso."
                        400 -> "Dados inválidos. Verifique o formato do e-mail ou a senha."
                        else -> "Erro no cadastro: Verifique seus dados ou tente mais tarde."
                    }
                    showErrorDialog = true
                }
            } catch (e: Exception) {
                errorMessage = "Falha na conexão com o servidor. Verifique sua internet."
                showErrorDialog = true
            } finally {
                isLoading = false
            }
        }
    }
}