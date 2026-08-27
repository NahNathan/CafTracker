package com.nathanrds.caftracker.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nathanrds.caftracker.domain.model.UnitType
import com.nathanrds.caftracker.presentation.viewmodel.AddEditProductViewModel
import com.nathanrds.caftracker.ui.theme.CoffeeOrange
import com.nathanrds.caftracker.ui.theme.CoffeeOrangeLight
import com.nathanrds.caftracker.ui.theme.PillShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    viewModel: AddEditProductViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEditMode = uiState.productId != null

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                modifier = Modifier.background(
                    Brush.verticalGradient(listOf(CoffeeOrangeLight, CoffeeOrange))
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                title = {
                    Text(
                        text = if (isEditMode) "Editar Produto" else "Adicionar Produto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionLabel(text = "Nome")
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Nome do produto") },
                shape = MaterialTheme.shapes.medium,
                colors = coffeeTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            SectionLabel(text = "Cafeína por Unidade")
            OutlinedTextField(
                value = uiState.caffeineMgPerUnitText,
                onValueChange = { viewModel.updateCaffeineMgPerUnit(it) },
                label = { Text("mg por unidade") },
                shape = MaterialTheme.shapes.medium,
                colors = coffeeTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            SectionLabel(text = "Tipo de Unidade")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UnitType.values().forEach { unitType ->
                    FilterChip(
                        selected = uiState.unitType == unitType,
                        onClick = { viewModel.updateUnitType(unitType) },
                        label = { Text(unitType.name) },
                        shape = MaterialTheme.shapes.small,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            SectionLabel(text = "Quantidade Padrão (opcional)")
            OutlinedTextField(
                value = uiState.defaultAmountText,
                onValueChange = { viewModel.updateDefaultAmount(it) },
                label = { Text("Quantidade padrão") },
                shape = MaterialTheme.shapes.medium,
                colors = coffeeTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            SectionLabel(text = "Notas (opcional)")
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = { viewModel.updateNotes(it) },
                label = { Text("Notas") },
                shape = MaterialTheme.shapes.medium,
                colors = coffeeTextFieldColors(),
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
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

            Button(
                onClick = { viewModel.saveProduct(onNavigateBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = PillShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isEditMode) "Atualizar" else "Salvar",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
