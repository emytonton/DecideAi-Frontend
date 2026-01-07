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
    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    private val sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

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
                    val body = response.body()
                    userName = body?.user?.username ?: "Usuário"
                    userToken = body?.accessToken ?: ""

                    if (userToken.isNotEmpty()) {
                        saveUserData(userToken, userName)
                        loginSuccess = true
                    } else {
                        errorMessage = "Erro ao recuperar token."
                    }
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

    fun resetState() {
        email = ""
        password = ""
        userName = ""
        userToken = ""
        isLoading = false
        errorMessage = null
        loginSuccess = false
    }

    private fun saveUserData(token: String, name: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
            putString("username", name)
            apply()
        }
    }
}