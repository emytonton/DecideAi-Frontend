package com.example.decideai_front.ui.features

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.decideai_front.viewmodel.GroupDecisionViewModel

@Composable
fun VotingScreen(
    decisionId: String,
    groupVm: GroupDecisionViewModel,
    token: String,
    onNavigateToResult: (String) -> Unit
) {
    val decision = groupVm.decisionsInbox.find { it.id == decisionId }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = decision?.title ?: "O que iremos assistir sábado a noite?",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Selecione a opção que preferir.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 16.dp),
            color = Color.Gray
        )

        // Nota: Idealmente a API de Inbox deveria retornar as opções.
        // Aqui usamos um placeholder caso o objeto decision não as contenha.
        val options = listOf("Terror", "Romance", "Ficção Científica", "Ação")

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(options) { option ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    onClick = { selectedOption = option },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    border = if (selectedOption == option) ButtonDefaults.outlinedButtonBorder else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = option, modifier = Modifier.weight(1f))
                        RadioButton(
                            selected = (selectedOption == option),
                            onClick = { selectedOption = option }
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                selectedOption?.let { option ->
                    groupVm.vote(token, decisionId, option) { winner ->
                        onNavigateToResult(winner)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91AFFF)),
            enabled = selectedOption != null
        ) {
            Text("Votar!", color = Color.White)
        }
    }
}