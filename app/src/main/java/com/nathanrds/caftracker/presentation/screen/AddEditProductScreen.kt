package com.nathanrds.caftracker.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nathanrds.caftracker.domain.model.UnitType
import com.nathanrds.caftracker.presentation.viewmodel.AddEditProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    viewModel: AddEditProductViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEditMode = uiState.productId != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Produto" else "Adicionar Produto") },
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
            // Nome
            Text(
                text = "Nome",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Nome do produto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Cafeína por unidade
            Text(
                text = "Cafeína por Unidade",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = uiState.caffeineMgPerUnitText,
                onValueChange = { viewModel.updateCaffeineMgPerUnit(it) },
                label = { Text("mg por unidade") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Tipo de unidade
            Text(
                text = "Tipo de Unidade",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UnitType.values().forEach { unitType ->
                    FilterChip(
                        selected = uiState.unitType == unitType,
                        onClick = { viewModel.updateUnitType(unitType) },
                        label = { Text(unitType.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quantidade padrão (opcional)
            Text(
                text = "Quantidade Padrão (opcional)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = uiState.defaultAmountText,
                onValueChange = { viewModel.updateDefaultAmount(it) },
                label = { Text("Quantidade padrão") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Notas (opcional)
            Text(
                text = "Notas (opcional)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = { viewModel.updateNotes(it) },
                label = { Text("Notas") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

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

            Spacer(modifier = Modifier.weight(1f))

            // Botão salvar
            Button(
                onClick = { viewModel.saveProduct(onNavigateBack) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text(if (isEditMode) "Atualizar" else "Salvar")
                }
            }
        }
    }
}