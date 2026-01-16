package com.example.decideai_front.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_settings")

class DataStorageManager(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("user_token")
        val NAME_KEY = stringPreferencesKey("user_name")
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    suspend fun saveSession(token: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[NAME_KEY] = name
        }
    }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = isDark
        }
    }

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val name: Flow<String?> = context.dataStore.data.map { it[NAME_KEY] ?: "Usuário" }
    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[DARK_MODE_KEY] ?: false }
}