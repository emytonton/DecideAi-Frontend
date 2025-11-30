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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.decideai_front.R
import com.example.decideai_front.viewmodel.OptionsDecisionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsDecisionScreen(onNavigateBack: () -> Unit, navController: NavHostController, userToken: String) {
    var listName by remember { mutableStateOf("") }
    var currentOption by remember { mutableStateOf("") }
    val optionsList = remember { mutableStateListOf<String>() }
    val viewModel: OptionsDecisionViewModel = viewModel()

    LaunchedEffect(viewModel.decisionResult) {
        viewModel.decisionResult?.let { result ->
            val encodedResult = java.net.URLEncoder.encode(result, "UTF-8")
            navController.navigate("options_result/$result")
            viewModel.clearResult()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Crie suas opções!", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Perfil */ }) {
                        Icon(painterResource(id = R.drawable.ic_person), contentDescription = null, modifier = Modifier.size(32.dp))
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

            // Campo para adicionar nova opção
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = currentOption,
                    onValueChange = { currentOption = it },
                    placeholder = { Text("Jogar GTA...", color = Color.LightGray) },
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

            // Lista de opções adicionadas
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(optionsList) { index, option ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ícone do dado pequeno à esquerda
                        Icon(
                            painter = painterResource(id = R.drawable.icon_dice),
                            contentDescription = null,
                            tint = Color(0xFFA5D6A7),
                            modifier = Modifier.size(32.dp).shadow(2.dp, CircleShape).background(Color.White, CircleShape).padding(4.dp)
                        )

                        OutlinedTextField(
                            value = option,
                            onValueChange = { /* Editar se necessário */ },
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

            // Botões de ação inferiores
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                Button(
                    onClick = { viewModel.makeDecisionOptions(listName, optionsList.toList(), userToken) },
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
                        onClick = { /* Salvar */ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC5E1A5)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Salvar") }

                    Button(
                        onClick = { /* Salvar e sortear */ },
                        modifier = Modifier.weight(1.2f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC5E1A5)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Salvar e sortear") }
                }
            }
        }
    }
}