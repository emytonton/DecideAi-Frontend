package com.example.decideai_front.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decideai_front.data.DataStorageManager
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStorageManager = DataStorageManager(application)

    var isDarkTheme by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            dataStorageManager.isDarkMode.collect { savedTheme ->
                isDarkTheme = savedTheme
            }
        }
    }

    fun toggleTheme() {
        val newTheme = !isDarkTheme
        isDarkTheme = newTheme

        viewModelScope.launch {
            dataStorageManager.saveTheme(newTheme)
        }
    }
}
