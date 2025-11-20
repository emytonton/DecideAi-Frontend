package com.example.decideai_front.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.LoginRequest
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

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
                val request = LoginRequest(email, password)
                val response = RetrofitClient.service.login(request)

                if (response.isSuccessful) {
                    loginSuccess = true
                } else {
                    errorMessage = "Login inválido: Verifique seus dados"
                }
            } catch (e: Exception) {
                errorMessage = "Falha na conexão com o servidor."
            } finally {
                isLoading = false
            }
        }
    }
}