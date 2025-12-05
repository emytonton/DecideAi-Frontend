package com.example.decideai_front.ui.features

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decideai_front.viewmodel.GroupDecisionViewModel

@Composable
fun GroupResultScreen(
    winner: String,
    decisionId: String,
    groupVm: GroupDecisionViewModel,
    token: String,
    onClose: () -> Unit
) {
    LaunchedEffect(Unit) {
        groupVm.markAsViewed(token, decisionId)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Decisão final!",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = winner,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            ),
            modifier = Modifier.padding(vertical = 32.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF91AFFF))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Resultados", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Total de votos: 5", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("1° $winner", color = Color.White.copy(alpha = 0.8f))
                    Text("3 votos", color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91AFFF))
        ) {
            Text("Encerrar", color = Color.White)
        }
    }
}