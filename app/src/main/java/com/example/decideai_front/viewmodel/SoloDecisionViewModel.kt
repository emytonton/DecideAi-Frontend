package com.example.decideai_front.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.SoloDecisionRequest
import com.example.decideai_front.data.model.SoloDecisionResponse
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import java.text.Normalizer

class SoloDecisionViewModel : ViewModel() {
    var decisionResult by mutableStateOf<SoloDecisionResponse?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun getDecision(token: String, category: String, filters: List<String>, option: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val normalizedCategory = category.lowercase()

                val sanitizedFilters = filters.map { it.normalizeForApi() }
                val filter1Value = if (sanitizedFilters.isNotEmpty()) sanitizedFilters.joinToString(" | ") else null

                val filter2Value = if (option.isNotEmpty()) option.normalizeForApi() else null

                val request = SoloDecisionRequest(
                    category = normalizedCategory,
                    filter1 = filter1Value,
                    filter2 = filter2Value
                )

                val response = RetrofitClient.service.makeSoloDecision("Bearer $token", request)

                if (response.isSuccessful) {
                    decisionResult = response.body()
                } else {
                    errorMessage = "Ops! Não encontramos nada com esses filtros."
                }
            } catch (e: Exception) {
                errorMessage = "Erro ao conectar com o servidor."
            } finally {
                isLoading = false
            }
        }
    }

    fun clearResult() {
        decisionResult = null
        errorMessage = null
    }

    private fun String.normalizeForApi(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return temp.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .trim()
    }
}