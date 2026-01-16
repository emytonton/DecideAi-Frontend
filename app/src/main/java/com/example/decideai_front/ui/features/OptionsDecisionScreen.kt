package com.example.decideai_front.ui.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.decideai_front.R
import com.example.decideai_front.ui.components.AppBottomBar
import com.example.decideai_front.ui.components.AppTopBar
import com.example.decideai_front.viewmodel.OptionsDecisionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsDecisionScreen(
    navController: NavController,
    userToken: String,
    viewModel: OptionsDecisionViewModel,
    avatarUrl: String? = null,
    listIdToEdit: String? = null,
    userName: String
) {
    var listName by remember { mutableStateOf("") }
    var currentOption by remember { mutableStateOf("") }
    val optionsList = remember { mutableStateListOf<String>() }

    LaunchedEffect(listIdToEdit) {
        if (listIdToEdit != null) {
            val list = viewModel.myLists.find { it.id == listIdToEdit }
            list?.let {
                listName = it.title
                optionsList.clear()
                optionsList.addAll(it.options)
            }
        }
    }

    LaunchedEffect(viewModel.decisionResult) {
        viewModel.decisionResult?.let { result ->
            navController.navigate("options_result/$result") {
                popUpTo("my_lists/$userToken") { inclusive = false }
            }
            viewModel.clearResult()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "DecideAí",
                navController = navController,
                userToken = userToken,
                avatarUrl = avatarUrl,
                showBackButton = false,
                showProfileIcon = true
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = null,
                userToken = userToken,
                userName = userName
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = if (listIdToEdit == null) "Crie suas opções!" else "Editar Lista",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Nome da Lista:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                OutlinedTextField(
                    value = listName,
                    onValueChange = { listName = it },
                    placeholder = { Text("O que vou jogar?", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .shadow(6.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Opções:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = currentOption,
                        onValueChange = { currentOption = it },
                        placeholder = { Text("Jogar GTA...", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .shadow(6.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                    IconButton(
                        onClick = {
                            if (currentOption.isNotBlank()) {
                                optionsList.add(currentOption)
                                currentOption = ""
                            }
                        },
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp)
                            .background(Color(0xFFAED581), CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(optionsList) { index, option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_dice),
                                    contentDescription = null,
                                    tint = Color(0xFFAED581),
                                    modifier = Modifier.size(24.dp)
                                )

                                Text(
                                    text = option,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 12.dp),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                IconButton(
                                    onClick = { optionsList.removeAt(index) },
                                    modifier = Modifier
                                        .background(Color(0xFFE57373), CircleShape)
                                        .size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.decideTemp(userToken, optionsList.toList()) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(8.dp, RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAED581)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.icon_dice),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Tome sua decisão!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.saveList(userToken, listIdToEdit, listName, optionsList.toList()) {
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFAED581),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                                Text("Salvar", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.saveAndDecide(userToken, listIdToEdit, listName, optionsList.toList())
                            },
                            modifier = Modifier
                                .weight(1.2f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFAED581),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            Text("Salvar e sortear", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}