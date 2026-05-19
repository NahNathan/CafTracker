package com.nathanrds.caftracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathanrds.caftracker.domain.usecase.intakes.AddIntakeUseCase
import com.nathanrds.caftracker.domain.usecase.products.GetProductsUseCase
import com.nathanrds.caftracker.presentation.uistate.AddIntakeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddIntakeViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addIntakeUseCase: AddIntakeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddIntakeUiState())
    val uiState: StateFlow<AddIntakeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            getProductsUseCase()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Erro ao carregar produtos: ${exception.message}"
                    )
                }
                .collect { products ->
                    _uiState.value = _uiState.value.copy(products = products)
                    // Atualizar computedTotalMg quando produtos carregarem
                    updateComputedTotalMg()
                }
        }
    }

    fun selectProduct(productId: Long) {
        _uiState.value = _uiState.value.copy(selectedProductId = productId)
        updateComputedTotalMg()
    }

    fun updateAmountText(text: String) {
        _uiState.value = _uiState.value.copy(amountText = text)
        updateComputedTotalMg()
    }

    private fun updateComputedTotalMg() {
        val productId = _uiState.value.selectedProductId
        val amountText = _uiState.value.amountText
        
        if (productId != null && amountText.isNotBlank()) {
            val product = _uiState.value.products.find { it.id == productId }
            val amount = amountText.toDoubleOrNull()
            
            if (product != null && amount != null && amount > 0) {
                val totalMg = amount * product.caffeineMgPerUnit
                _uiState.value = _uiState.value.copy(computedTotalMg = totalMg)
            } else {
                _uiState.value = _uiState.value.copy(computedTotalMg = null)
            }
        } else {
            _uiState.value = _uiState.value.copy(computedTotalMg = null)
        }
    }

    fun saveIntake(onSuccess: () -> Unit) {
        val state = _uiState.value
        val productId = state.selectedProductId
        val amount = state.amountText.toDoubleOrNull()

        if (productId == null) {
            _uiState.value = _uiState.value.copy(errorMessage = "Selecione um produto")
            return
        }

        if (amount == null || amount <= 0) {
            _uiState.value = _uiState.value.copy(errorMessage = "Digite uma quantidade válida")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)
            
            val intake = com.nathanrds.caftracker.domain.model.Intake(
                productId = productId,
                amount = amount,
                timestampMillis = System.currentTimeMillis()
            )
            
            val result = addIntakeUseCase(intake)
            
            when (result) {
                is com.nathanrds.caftracker.domain.util.Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        selectedProductId = null,
                        amountText = "",
                        computedTotalMg = null
                    )
                    onSuccess()
                }
                is com.nathanrds.caftracker.domain.util.Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}