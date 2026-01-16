package com.example.decideai_front.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.LoginRequest
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var userName by mutableStateOf("")
    var userToken by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var showErrorDialog by mutableStateOf(false)
    var loginSuccess by mutableStateOf(false)

    private val sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun onLoginClick() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, preencha todos os campos."
            showErrorDialog = true
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.service.login(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    val token = body?.accessToken ?: ""
                    val name = body?.user?.username ?: "Usuário"

                    if (token.isNotEmpty()) {
                        userName = name
                        userToken = token
                        saveUserData(token, name)
                        loginSuccess = true
                    } else {
                        errorMessage = "Erro ao recuperar dados de acesso."
                        showErrorDialog = true
                    }
                } else {
                    errorMessage = when (response.code()) {
                        404 -> "E-mail não encontrado. Verifique se digitou corretamente."
                        401 -> "Senha incorreta. Tente novamente."
                        else -> "Falha no login: Verifique seus dados."
                    }
                    showErrorDialog = true
                }
            } catch (e: Exception) {
                errorMessage = "Não foi possível conectar ao servidor. Verifique sua conexão."
                showErrorDialog = true
            } finally {
                isLoading = false
            }
        }
    }

    fun resetState() {
        email = ""
        password = ""
        userName = ""
        userToken = ""
        isLoading = false
        errorMessage = ""
        showErrorDialog = false
        loginSuccess = false
    }

    private fun saveUserData(token: String, name: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
            putString("name", name)
            apply()
        }
    }
}