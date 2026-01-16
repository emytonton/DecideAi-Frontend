package com.example.decideai_front.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppErrorDialog(
    showDialog: Boolean,
    title: String = "Ops!",
    message: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Entendi",
                        color = Color(0xFF324290),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            title = {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            },
            text = {
                Text(text = message, fontSize = 16.sp)
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}