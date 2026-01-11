package com.example.decideai_front.ui.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decideai_front.R

@Composable
fun DecisionResultScreen(
    title: String,
    details: String,
    onAccept: () -> Unit,
    onReface: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF9191FF))
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("SUGESTÃO:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                Text(
                    text = title.uppercase(),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White, thickness = 2.dp)
                    Icon(
                        painter = painterResource(id = R.drawable.icon_dice),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(80.dp).padding(horizontal = 16.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White, thickness = 2.dp)
                }

                Text("DETALHES:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(details, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onAccept,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9191FF)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Aceitar", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onReface,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
        ) {
            Text("Refazer", fontSize = 18.sp, color = Color.Black)
        }
    }
}