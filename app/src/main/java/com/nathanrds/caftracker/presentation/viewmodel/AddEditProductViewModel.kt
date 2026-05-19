package com.nathanrds.caftracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathanrds.caftracker.domain.model.Product
import com.nathanrds.caftracker.domain.model.UnitType
import com.nathanrds.caftracker.domain.repository.ProductsRepository
import com.nathanrds.caftracker.domain.usecase.products.AddProductUseCase
import com.nathanrds.caftracker.domain.usecase.products.UpdateProductUseCase
import com.nathanrds.caftracker.presentation.uistate.AddEditProductUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEditProductViewModel(
    private val productsRepository: ProductsRepository,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    productId: Long? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditProductUiState(productId = productId))
    val uiState: StateFlow<AddEditProductUiState> = _uiState.asStateFlow()

    init {
        if (productId != null) {
            loadProduct(productId)
        }
    }

    private fun loadProduct(id: Long) {
        viewModelScope.launch {
            try {
                val product = productsRepository.getProductById(id)
                if (product != null) {
                    _uiState.value = _uiState.value.copy(
                        name = product.name,
                        caffeineMgPerUnitText = product.caffeineMgPerUnit.toString(),
                        unitType = product.unitType,
                        defaultAmountText = product.defaultAmount?.toString() ?: "",
                        notes = product.notes ?: ""
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Erro ao carregar produto: ${e.message}")
            }
        }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateCaffeineMgPerUnit(text: String) {
        _uiState.value = _uiState.value.copy(caffeineMgPerUnitText = text)
    }

    fun updateUnitType(unitType: UnitType) {
        _uiState.value = _uiState.value.copy(unitType = unitType)
    }

    fun updateDefaultAmount(text: String) {
        _uiState.value = _uiState.value.copy(defaultAmountText = text)
    }

    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun saveProduct(onSuccess: () -> Unit) {
        val state = _uiState.value
        
        val caffeineMgPerUnit = state.caffeineMgPerUnitText.toDoubleOrNull()
        val defaultAmount = state.defaultAmountText.toDoubleOrNull()

        if (state.name.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Nome não pode estar vazio")
            return
        }

        if (caffeineMgPerUnit == null || caffeineMgPerUnit <= 0) {
            _uiState.value = _uiState.value.copy(errorMessage = "Cafeína por unidade deve ser maior que zero")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)

            val product = Product(
                id = state.productId ?: 0L,
                name = state.name,
                caffeineMgPerUnit = caffeineMgPerUnit,
                unitType = state.unitType,
                defaultAmount = defaultAmount,
                notes = state.notes.takeIf { it.isNotBlank() }
            )

            val result = if (state.productId != null) {
                updateProductUseCase(product)
            } else {
                addProductUseCase(product)
            }

            when (result) {
                is com.nathanrds.caftracker.domain.util.Result.Success -> {
                    _uiState.value = _uiState.value.copy(isSaving = false)
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