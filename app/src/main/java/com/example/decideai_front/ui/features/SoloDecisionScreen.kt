package com.example.decideai_front.ui.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.decideai_front.R
import com.example.decideai_front.ui.components.AppBottomBar
import com.example.decideai_front.ui.components.AppTopBar
import com.example.decideai_front.viewmodel.SoloDecisionViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SoloDecisionScreen(onNavigateBack: () -> Unit, userToken: String, navController: NavHostController, userName: String) {
    val viewModel: SoloDecisionViewModel = viewModel()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val apiCategories = listOf("movie", "food", "drink", "exercise", "book")
    val displayCategories = listOf("Filme", "Comida", "Bebida", "Exercício", "Livro")
    val icons = listOf(R.drawable.icon_cinema, R.drawable.icon_food, R.drawable.icon_drink, R.drawable.icon_run, R.drawable.icon_book)

    val selectedFilters = remember { mutableStateListOf<String>() }
    var selectedDropdownOption by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val currentCategory = apiCategories[selectedTabIndex]

    val (filterLabel, dropdownLabel, filterOptions, dropdownOptions) = when (currentCategory) {
        "movie" -> CategoryConfig("Gênero:", "Streaming:", listOf("Ação", "Comédia", "Drama", "Terror", "Romance", "Ficção"), listOf("Netflix", "Prime Video", "Disney", "HBO"))
        "food" -> CategoryConfig("Tipo:", "Refeição:", listOf("Japonesa", "Italiana", "Fit", "Chinesa", "Brasileira", "Americana"), listOf("Almoço", "Jantar", "Lanche"))
        "drink" -> CategoryConfig("Tipo:", "Opções:", listOf("Drinks", "Suco", "Chá", "Destilado", "Refrigerante", "MilkShake"), listOf("Doce", "Quente", "Refrescante"))
        "exercise" -> CategoryConfig("Tipo:", "Horário:", listOf("Musculação", "Caminhada", "Vôlei", "Corrida", "Aeróbico", "Futebol"), listOf("Diurno", "Noturno"))
        "book" -> CategoryConfig("Gênero:", "Tamanho:", listOf("Fantasia", "Romance", "Aventura", "Terror", "Humor", "Ficção"), listOf("Pequena", "Grande", "Médio"))
        else -> CategoryConfig("Filtro:", "Opção:", emptyList(), emptyList())
    }

    LaunchedEffect(viewModel.decisionResult) {
        viewModel.decisionResult?.let { result ->
            val safeTitle = URLEncoder.encode(result.title, "UTF-8")
            val safeDetails = URLEncoder.encode(result.details, "UTF-8")

            navController.navigate("decision_result/$safeTitle/$safeDetails/$userName/$userToken")
            viewModel.clearResult()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "DecideAí",
                navController = navController,
                userToken = userToken,
                showBackButton = false,
                showProfileIcon = true
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = "solo_decision/$userToken",
                userToken = userToken
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Decisão rápida",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "O que vamos decidir hoje?",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    displayCategories.forEachIndexed { index, title ->
                        val isSelected = selectedTabIndex == index
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    selectedTabIndex = index
                                    selectedFilters.clear()
                                    selectedDropdownOption = ""
                                }
                                .padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = icons[index]),
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFFE57373) else Color.Gray,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = title,
                                color = if (isSelected) Color(0xFFE57373) else Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .height(2.dp)
                                        .width(32.dp)
                                        .background(Color(0xFFE57373))
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                )

                Text(
                    text = "Filtre suas preferências:",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(text = filterLabel, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    filterOptions.forEach { option ->
                        val isSelected = selectedFilters.contains(option)
                        FilterChip(
                            selected = isSelected,
                            onClick = { if (isSelected) selectedFilters.remove(option) else selectedFilters.add(option) },
                            label = { Text(option, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp)) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                            } else null,
                            shape = RoundedCornerShape(24.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFFF2F2F2),
                                labelColor = Color.DarkGray,
                                selectedContainerColor = Color(0xFFFFDAB9),
                                selectedLabelColor = Color(0xFFE65100)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = Color.LightGray.copy(alpha = 0.5f),
                                selectedBorderColor = Color.Transparent,
                                borderWidth = 1.dp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = dropdownLabel, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedDropdownOption,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text(dropdownOptions.first(), color = Color.Gray) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        dropdownOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = { selectedDropdownOption = option; expanded = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                ) {
                    Text(
                        text = "Tome sua decisão!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        onClick = {
                            viewModel.getDecision(userToken, currentCategory, selectedFilters, selectedDropdownOption)
                        },
                        modifier = Modifier
                            .size(120.dp)
                            .shadow(12.dp, CircleShape),
                        shape = CircleShape,
                        color = Color(0xFF91AFFF)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (viewModel.isLoading) {
                                CircularProgressIndicator(color = Color.White)
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_dice),
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class CategoryConfig(val filterLabel: String, val dropdownLabel: String, val filterOptions: List<String>, val dropdownOptions: List<String>)