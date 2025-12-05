package com.example.decideai_front.ui.features

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.decideai_front.viewmodel.GroupDecisionViewModel

@Composable
fun CreateGroupDecisionScreen(
    groupVm: GroupDecisionViewModel,
    userToken: String,
    onNavigateToInvite: () -> Unit,
    onNavigateToInbox: () -> Unit
) {
    var currentOption by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Decida em grupo!",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = groupVm.tempTitle,
            onValueChange = { groupVm.tempTitle = it },
            label = { Text("Decisão à tomar:") },
            placeholder = { Text("O que iremos assistir?") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Opções:")

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = currentOption,
                onValueChange = { currentOption = it },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                if (currentOption.isNotBlank()) {
                    groupVm.tempOptions.add(currentOption)
                    currentOption = ""
                }
            }) {
                Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color(0xFF91AFFF))
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(groupVm.tempOptions) { option ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = option)
                    IconButton(onClick = { groupVm.tempOptions.remove(option) }) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                    }
                }
            }
        }

        Button(
            onClick = { onNavigateToInvite() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = groupVm.tempTitle.isNotBlank() && groupVm.tempOptions.size >= 2,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91AFFF))
        ) {
            Text(text = "Chame seus amigos", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onNavigateToInbox() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF91AFFF))
        ) {
            Text(text = "Decisões em andamento", color = Color.White)
        }
    }
}