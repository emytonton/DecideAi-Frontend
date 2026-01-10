package com.example.decideai_front.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefs = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var isDarkTheme by mutableStateOf(sharedPrefs.getBoolean("is_dark_mode", false))
        private set
    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
        sharedPrefs.edit().putBoolean("is_dark_mode", isDarkTheme).apply()
    }
}
