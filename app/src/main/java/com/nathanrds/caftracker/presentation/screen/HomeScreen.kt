package com.nathanrds.caftracker.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nathanrds.caftracker.presentation.uistate.TodayIntakeItem
import com.nathanrds.caftracker.presentation.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToProducts: () -> Unit,
    onNavigateToAddIntake: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("CafTracker") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddIntake) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mensagem de erro
            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.errorMessage ?: "",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            // Loading indicator
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            // Total do dia
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total de Hoje",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${String.format(Locale.getDefault(), "%.1f", uiState.todayTotalMg)} mg",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Lista de consumos de hoje
            Text(
                text = "Consumos de Hoje",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (uiState.todayIntakes.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Nenhum consumo registrado hoje",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.todayIntakes) { intake ->
                        TodayIntakeCard(intake)
                    }
                }
            }

            // Últimos 7 dias
            Text(
                text = "Últimos 7 Dias",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (uiState.last7Days.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Nenhum dado disponível",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.last7Days.forEach { dailyTotal ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val date = LocalDate.ofEpochDay(dailyTotal.dateEpochDay)
                                val formatter = DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault())
                                Text(
                                    text = formatter.format(date),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${String.format(Locale.getDefault(), "%.1f", dailyTotal.totalMg)} mg",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Botão para produtos
            Button(
                onClick = onNavigateToProducts,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gerenciar Produtos")
            }
        }
    }
}

@Composable
fun TodayIntakeCard(intake: TodayIntakeItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = intake.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${String.format(Locale.getDefault(), "%.1f", intake.amount)} ${intake.unitLabel}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${String.format(Locale.getDefault(), "%.1f", intake.totalMg)} mg",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = intake.timeLabel,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}