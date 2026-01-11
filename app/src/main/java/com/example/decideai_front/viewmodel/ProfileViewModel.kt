package com.example.decideai_front.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.UpdateProfileRequest
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var avatar by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var updateSuccess by mutableStateOf(false)


    fun resetState() {
        username = ""
        email = ""
        avatar = null
        isLoading = false
        updateSuccess = false
    }


    fun loadProfile(token: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.service.getProfile("Bearer $token")
                if (response.isSuccessful) {
                    val user = response.body()
                    username = user?.username ?: ""
                    email = user?.email ?: ""
                    avatar = user?.avatar
                }
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun updateProfile(token: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val request = UpdateProfileRequest(username, email, avatar)
                val response = RetrofitClient.service.updateProfile("Bearer $token", request)
                if (response.isSuccessful) updateSuccess = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteAccount(token: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.service.deleteAccount("Bearer $token")

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Erro ao excluir: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Erro de conexão: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateAvatar(newUrl: String) {
        avatar = newUrl
    }
}