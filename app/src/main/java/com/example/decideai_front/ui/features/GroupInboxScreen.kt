package com.example.decideai_front.ui.features

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.decideai_front.viewmodel.GroupDecisionViewModel

@Composable
fun GroupInboxScreen(
    groupVm: GroupDecisionViewModel,
    token: String,
    onNavigateToVote: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        groupVm.loadInbox(token)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Decisões esperando por você!",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(groupVm.decisionsInbox) { decision ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = decision.title, modifier = Modifier.weight(1f))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { onNavigateToVote(decision.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91AFFF)),
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                            ) {
                                Text("Votar", color = Color.White)
                            }

                            IconButton(onClick = { /* Lógica para recusar */ }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}