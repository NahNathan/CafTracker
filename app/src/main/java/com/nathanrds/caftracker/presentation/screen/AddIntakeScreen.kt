package com.nathanrds.caftracker.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nathanrds.caftracker.presentation.viewmodel.AddIntakeViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIntakeScreen(
    viewModel: AddIntakeViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Consumo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Seleção de produto
            Text(
                text = "Produto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (uiState.products.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Nenhum produto cadastrado. Adicione produtos primeiro.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                var expanded by remember { mutableStateOf(false) }
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.products.find { it.id == uiState.selectedProductId }?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Selecione um produto") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        uiState.products.forEach { product ->
                            DropdownMenuItem(
                                text = { Text(product.name) },
                                onClick = {
                                    viewModel.selectProduct(product.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Campo de quantidade
            Text(
                text = "Quantidade",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = uiState.amountText,
                onValueChange = { viewModel.updateAmountText(it) },
                label = { Text("Quantidade") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Preview do total de mg
            uiState.computedTotalMg?.let { totalMg ->
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
                            text = "Total de Cafeína",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${String.format(Locale.getDefault(), "%.1f", totalMg)} mg",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Mensagem de erro
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botão salvar
            Button(
                onClick = { viewModel.saveIntake(onNavigateBack) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving && uiState.selectedProductId != null && uiState.amountText.isNotBlank()
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Salvar")
                }
            }
        }
    }
}