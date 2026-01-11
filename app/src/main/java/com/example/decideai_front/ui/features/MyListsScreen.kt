package com.example.decideai_front.ui.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.decideai_front.viewmodel.OptionsDecisionViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListsScreen(
    navController: NavController,
    token: String,
    viewModel: OptionsDecisionViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.loadUserLists(token)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Minhas Listas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {

                    navController.navigate("manage_list/$token")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAED581)), // Verde claro
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Criar nova lista", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))


            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(viewModel.myLists) { list ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .shadow(2.dp, RoundedCornerShape(12.dp))
                            .clickable {

                                navController.navigate("manage_list/$token?listId=${list.id}")
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(list.title, fontSize = 16.sp, fontWeight = FontWeight.Medium)



                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE57373), CircleShape)
                                    .clickable { viewModel.deleteList(token, list.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Deletar",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}