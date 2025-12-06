package com.example.decideai_front.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.*
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class GroupDecisionViewModel : ViewModel() {

    var decisionsInbox by mutableStateOf<List<GroupDecisionSummary>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var tempTitle by mutableStateOf("")
    var tempOptions = mutableStateListOf<String>()
    var selectedFriendsIds = mutableStateListOf<String>()

    fun loadInbox(token: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.service.getGroupDecisions("Bearer $token")
                if (response.isSuccessful) {
                    decisionsInbox = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar decisões"
            } finally {
                isLoading = false
            }
        }
    }

    fun createDecision(token: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val request = GroupDecisionRequest(tempTitle, tempOptions.toList(), selectedFriendsIds.toList())
                val response = RetrofitClient.service.createGroupDecision("Bearer $token", request)
                if (response.isSuccessful) {
                    tempTitle = ""
                    tempOptions.clear()
                    selectedFriendsIds.clear()
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage = "Falha ao criar grupo"
            }
        }
    }

    fun vote(token: String, decisionId: String, option: String, onFinished: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.service.answerGroupDecision(
                    "Bearer $token",
                    GroupVoteRequest(decisionId, voteOption = option)
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "finished") {
                        body.winner?.let { onFinished(it) }
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Erro ao enviar voto"
            }
        }
    }

    fun markAsViewed(token: String, decisionId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.service.markDecisionAsViewed(
                    "Bearer $token",
                    MarkAsViewedRequest(decisionId)
                )
                if (response.isSuccessful) {
                    loadInbox(token)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}