package com.example.decideai_front.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.*
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class FriendsViewModel : ViewModel() {


    var friendsList by mutableStateOf<List<UserResponse>>(emptyList())
    var pendingRequests by mutableStateOf<List<FriendRequest>>(emptyList())


    var searchResults by mutableStateOf<List<UserResponse>>(emptyList())
    var searchQuery by mutableStateOf("")


    var sentRequestIds by mutableStateOf<List<String>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)


    fun loadFriends(token: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.service.getFriends("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let {
                        friendsList = it.friends
                        pendingRequests = it.pendingReceived
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar amigos"
            } finally {
                isLoading = false
            }
        }
    }


    fun searchUsers(token: String, query: String) {
        searchQuery = query
        if (query.isEmpty()) {
            searchResults = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitClient.service.searchUsers("Bearer $token", query)
                if (response.isSuccessful) {
                    searchResults = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun sendRequest(token: String, userId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.service.sendFriendRequest("Bearer $token", SendFriendRequest(userId))
                if (response.isSuccessful) {

                    sentRequestIds = sentRequestIds + userId
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage = "Falha ao enviar solicitação"
            }
        }
    }


    fun answerRequest(token: String, requestId: String, action: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.service.answerFriendRequest(
                    "Bearer $token",
                    AnswerFriendRequest(requestId, action)
                )
                if (response.isSuccessful) {

                    loadFriends(token)
                }
            } catch (e: Exception) {
                errorMessage = "Erro ao processar solicitação"
            }
        }
    }


    fun removeFriend(token: String, friendId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.service.removeFriend("Bearer $token", RemoveFriendRequest(friendId))
                if (response.isSuccessful) {

                    friendsList = friendsList.filter { it.id != friendId }
                }
            } catch (e: Exception) {
                errorMessage = "Erro ao remover amigo"
            }
        }
    }
}