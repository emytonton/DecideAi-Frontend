package com.example.decideai_front.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.OptionsDecisionRequest
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class OptionsDecisionViewModel : ViewModel() {
    var decisionResult by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun makeDecisionOptions(listName: String, options: List<String>, token: String) {
        if (options.size < 2) return

        viewModelScope.launch {
            isLoading = true
            try {
                val request = OptionsDecisionRequest(tempOptions = options)
                val response = RetrofitClient.service.makeOptionsDecision("Bearer $token", OptionsDecisionRequest(options))

                if (response.isSuccessful) {
                    decisionResult = response.body()?.result
                } else {
                    errorMessage = "Erro ao realizar o sorteio."
                }
            } catch (e: Exception) {
                errorMessage = "Falha na conexão."
            } finally {
                isLoading = false
            }
        }
    }

    fun clearResult() {
        decisionResult = null
    }
}