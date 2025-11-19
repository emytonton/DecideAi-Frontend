package com.example.decideai_front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.decideai_front.ui.features.LoginScreen
import com.example.decideai_front.ui.theme.DecideAiFrontTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DecideAiFrontTheme {
                // O Scaffold é a estrutura base. Vamos colocar sua tela dentro dele.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Chamamos sua LoginScreen aqui passando o padding para evitar sobrepor as barras do sistema
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}