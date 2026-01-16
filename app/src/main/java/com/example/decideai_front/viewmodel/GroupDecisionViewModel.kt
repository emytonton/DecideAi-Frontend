package com.example.decideai_front.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.*
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class GroupDecisionViewModel : ViewModel() {


    var groupDecisions by mutableStateOf<List<GroupDecisionSummary>>(emptyList())
    var currentDecision by mutableStateOf<GroupDecisionDetail?>(null)

    var friendsToInvite by mutableStateOf<List<UserResponse>>(emptyList())
    val selectedFriendIds = mutableStateListOf<String>()

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var voteResult by mutableStateOf<VoteGroupResponse?>(null)


    val pendingDecisions: List<GroupDecisionSummary>
        get() = groupDecisions.filter { it.status == "open" && !it.hasVoted && it.myStatus != "declined" }


    val inProgressDecisions: List<GroupDecisionSummary>
        get() = groupDecisions.filter { it.status == "open" && it.hasVoted }


    val finishedDecisions: List<GroupDecisionSummary>
        get() = groupDecisions.filter { it.status == "finished" && it.myStatus != "declined" }



    fun loadDecisions(token: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = RetrofitClient.service.getGroupDecisions("Bearer $token")
                if (response.isSuccessful) {
                    groupDecisions = response.body() ?: emptyList()
                } else {
                    errorMessage = "Erro ao carregar lista: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Falha: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadDecisionDetails(token: String, id: String) {
        viewModelScope.launch {
            isLoading = true
            currentDecision = null
            try {
                val response = RetrofitClient.service.getGroupDecisionDetails("Bearer $token", id)
                if (response.isSuccessful) {
                    currentDecision = response.body()
                }
            } catch (e: Exception) {
                errorMessage = "Erro de conexão."
            } finally {
                isLoading = false
            }
        }
    }


    fun loadFriendsForSelection(token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.service.getFriends("Bearer $token")
                if (response.isSuccessful) friendsToInvite = response.body()?.friends ?: emptyList()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun toggleFriendSelection(friendId: String) {
        if (selectedFriendIds.contains(friendId)) selectedFriendIds.remove(friendId) else selectedFriendIds.add(friendId)
    }

    fun createDecision(token: String, title: String, options: List<String>, onSuccess: () -> Unit) {
        if (title.isBlank() || options.size < 2 || selectedFriendIds.isEmpty()) return

        viewModelScope.launch {
            isLoading = true
            try {
                val request = CreateGroupRequest(title, options, selectedFriendIds.toList())
                val response = RetrofitClient.service.createGroupDecision("Bearer $token", request)
                if (response.isSuccessful || response.code() == 201) {
                    selectedFriendIds.clear()
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage = "Erro ao criar."
            } finally {
                isLoading = false
            }
        }
    }


    fun vote(token: String, decisionId: String, option: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val request = VoteGroupRequest(decisionId = decisionId, voteOption = option)
                val response = RetrofitClient.service.voteGroupDecision("Bearer $token", request)

                if (response.isSuccessful) {
                    voteResult = response.body()


                    groupDecisions = groupDecisions.map {
                        if (it.id == decisionId) {
                            it.copy(
                                status = voteResult?.status ?: it.status,
                                winner = voteResult?.winner,
                                myStatus = voteResult?.myStatus ?: "accepted",
                                hasVoted = voteResult?.hasVoted ?: true,
                                myVote = voteResult?.myVote ?: option
                            )
                        } else it
                    }

                    val isFinished = voteResult?.status == "finished"
                    onSuccess(isFinished)
                } else {
                    errorMessage = "Erro ao votar."
                }
            } catch (e: Exception) {
                errorMessage = "Erro: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }


    fun decline(token: String, decisionId: String) {
        viewModelScope.launch {
            try {
                val request = VoteGroupRequest(decisionId = decisionId, decline = true)
                val response = RetrofitClient.service.voteGroupDecision("Bearer $token", request)
                if (response.isSuccessful) {
                    groupDecisions = groupDecisions.filter { it.id != decisionId }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun markAsViewed(token: String, decisionId: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.service.markGroupResultAsViewed("Bearer $token", MarkViewedRequest(decisionId))
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}