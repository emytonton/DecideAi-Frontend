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
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun onRegisterClick() {
        if (password != confirmPassword) {
            errorMessage = "As senhas não coincidem."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // Monta o objeto baseado na página 1 da documentação
                val request = RegisterRequest(username, email, password)
                val response = RetrofitClient.instance.signUp(request)

                if (response.isSuccessful) {
                    isSuccess = true // Navegar para o Login após sucesso
                } else {
                    // Trata erros 400 (validação) ou 409 (conflito) conforme pág 2
                    errorMessage = "Erro: ${response.code()} - Verifique os dados"
                }
            } catch (e: Exception) {
                errorMessage = "Falha na conexão com o servidor."
            } finally {
                isLoading = false
            }
        }
    }
}