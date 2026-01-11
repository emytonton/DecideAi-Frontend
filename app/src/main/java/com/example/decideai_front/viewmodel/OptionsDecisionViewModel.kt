package com.example.decideai_front.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.model.*
import com.example.decideai_front.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class OptionsDecisionViewModel : ViewModel() {


    var myLists by mutableStateOf<List<CustomListResponse>>(emptyList())
    var decisionResult by mutableStateOf<String?>(null)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)



    fun loadUserLists(token: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.service.getUserLists("Bearer $token")
                if (response.isSuccessful) {
                    myLists = response.body() ?: emptyList()
                } else {
                    errorMessage = "Erro ao carregar listas."
                }
            } catch (e: Exception) {
                errorMessage = "Falha na conexão."
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteList(token: String, listId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.service.deleteList("Bearer $token", listId)
                if (response.isSuccessful) {

                    myLists = myLists.filter { it.id != listId }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun saveList(
        token: String,
        listId: String?,
        title: String,
        options: List<String>,
        onSuccess: () -> Unit
    ) {
        if (title.isBlank() || options.size < 2) {
            errorMessage = "Preencha o título e insira pelo menos 2 opções."
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val request = SaveListRequest(title, options)

                val response = if (listId == null) {

                    RetrofitClient.service.createList("Bearer $token", request)
                } else {

                    RetrofitClient.service.updateList("Bearer $token", listId, request)
                }

                if (response.isSuccessful) {

                    loadUserLists(token)
                    onSuccess()
                } else {
                    errorMessage = "Erro ao salvar lista."
                }
            } catch (e: Exception) {
                errorMessage = "Erro de conexão."
            } finally {
                isLoading = false
            }
        }
    }


    fun saveAndDecide(
        token: String,
        listId: String?,
        title: String,
        options: List<String>
    ) {
        if (title.isBlank() || options.size < 2) {
            errorMessage = "Preencha o título e insira pelo menos 2 opções."
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val request = SaveListRequest(title, options)
                var finalListId = listId


                if (listId == null) {
                    val createRes = RetrofitClient.service.createList("Bearer $token", request)
                    if (createRes.isSuccessful) {
                        finalListId = createRes.body()?.id
                    } else {
                        errorMessage = "Erro ao criar lista."
                        return@launch
                    }
                } else {
                    val updateRes = RetrofitClient.service.updateList("Bearer $token", listId, request)
                    if (!updateRes.isSuccessful) {
                        errorMessage = "Erro ao atualizar lista."
                        return@launch
                    }
                }


                if (finalListId != null) {
                    val decideRes = RetrofitClient.service.makeOptionsDecision(
                        "Bearer $token",
                        OptionsDecisionRequest(listId = finalListId)
                    )
                    if (decideRes.isSuccessful) {
                        decisionResult = decideRes.body()?.result
                    } else {
                        errorMessage = "Erro ao realizar sorteio."
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Erro de conexão."
            } finally {
                isLoading = false
            }
        }
    }


    fun decideTemp(token: String, options: List<String>) {
        if (options.size < 2) {
            errorMessage = "Insira pelo menos 2 opções."
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {

                val request = OptionsDecisionRequest(tempOptions = options)
                val response = RetrofitClient.service.makeOptionsDecision("Bearer $token", request)

                if (response.isSuccessful) {
                    decisionResult = response.body()?.result
                } else {
                    errorMessage = "Erro ao realizar sorteio."
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
        errorMessage = null
    }
}