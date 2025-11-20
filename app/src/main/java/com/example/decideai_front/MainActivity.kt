package com.example.decideai_front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.decideai_front.ui.NavGraph
import com.example.decideai_front.ui.theme.DecideAiFrontTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DecideAiFrontTheme {
                Surface (color = MaterialTheme.colorScheme.background){
                    NavGraph()
                }
            }
        }
    }
}