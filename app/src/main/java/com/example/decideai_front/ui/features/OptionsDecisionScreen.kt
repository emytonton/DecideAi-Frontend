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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.decideai_front.R
import com.example.decideai_front.viewmodel.OptionsDecisionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsDecisionScreen(
    navController: NavController,
    userToken: String,
    viewModel: OptionsDecisionViewModel,
    listIdToEdit: String? = null
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
            CenterAlignedTopAppBar(
                title = {
                    Text(if(listIdToEdit == null) "Crie suas opções!" else "Editar Lista", fontSize = 18.sp)
                },
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

            Text("Nome da Lista:", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = listName,
                onValueChange = { listName = it },
                placeholder = { Text("Preencha aqui...", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).shadow(4.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Opções:", fontWeight = FontWeight.Bold)


            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = currentOption,
                    onValueChange = { currentOption = it },
                    placeholder = { Text("Adicionar opção...", color = Color.LightGray) },
                    modifier = Modifier.weight(1f).padding(vertical = 8.dp).shadow(2.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                    )
                )
                IconButton(
                    onClick = {
                        if (currentOption.isNotBlank()) {
                            optionsList.add(currentOption)
                            currentOption = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp).background(Color(0xFFC8E6C9), CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar", tint = Color.White)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Lista de opções
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(optionsList) { index, option ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_dice),
                            contentDescription = null,
                            tint = Color(0xFFA5D6A7),
                            modifier = Modifier.size(32.dp).shadow(2.dp, CircleShape).background(Color.White, CircleShape).padding(4.dp)
                        )

                        OutlinedTextField(
                            value = option,
                            onValueChange = {},
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp).shadow(2.dp, RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            readOnly = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                            )
                        )

                        IconButton(
                            onClick = { optionsList.removeAt(index) },
                            modifier = Modifier.background(Color(0xFFE57373), CircleShape).size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Remover", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }


            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {

                Button(
                    onClick = { viewModel.decideTemp(userToken, optionsList.toList()) },
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC5E1A5)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(painterResource(id = R.drawable.icon_dice), contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Tome sua decisão!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    Button(
                        onClick = {
                            viewModel.saveList(userToken, listIdToEdit, listName, optionsList.toList()) {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC5E1A5)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Salvar") }


                    Button(
                        onClick = {
                            viewModel.saveAndDecide(userToken, listIdToEdit, listName, optionsList.toList())
                        },
                        modifier = Modifier.weight(1.2f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC5E1A5)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Salvar e sortear") }
                }
            }
        }
    }
}