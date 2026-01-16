package com.example.decideai_front

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import com.example.decideai_front.data.DataStorageManager
import com.example.decideai_front.ui.features.NavGraph
import com.example.decideai_front.ui.theme.DecideAiFrontTheme
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataStorageManager = DataStorageManager(this)

        setContent {
            val token by dataStorageManager.token.collectAsState(initial = null)
            val name by dataStorageManager.name.collectAsState(initial = "Usuário")
            val isDark by dataStorageManager.isDarkMode.collectAsState(initial = false)

            DecideAiFrontTheme(darkTheme = isDark) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavGraph(
                        startToken = token,
                        startName = name,
                        initialDarkMode = isDark
                    )
                }
            }
        }
    }
}