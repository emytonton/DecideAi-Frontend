package com.example.decideai_front.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Estados da tela (O que o usuário digita)
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // Estado de carregamento e mensagens
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    fun onLoginClick() {
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage = "Preencha todos os campos"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                // Aqui chamaremos o Retrofit (DecideAiService)
                val request = LoginRequest(email, password)

                // Simulação de chamada (vamos configurar o Retrofit no próximo passo)
                // val response = RetrofitClient.service.login(request)

                // Se sucesso:
                loginSuccess = true

            } catch (e: Exception) {
                errorMessage = "Erro de conexão: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}