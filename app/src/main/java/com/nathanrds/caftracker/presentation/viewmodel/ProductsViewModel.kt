package com.nathanrds.caftracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathanrds.caftracker.domain.usecase.products.DeleteProductUseCase
import com.nathanrds.caftracker.domain.usecase.products.GetProductsUseCase
import com.nathanrds.caftracker.presentation.uistate.ProductsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                getProductsUseCase()
                    .catch { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Erro ao carregar produtos: ${exception.message}"
                        )
                    }
                    .collect { products ->
                        _uiState.value = _uiState.value.copy(
                            products = products,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar produtos: ${e.message}"
                )
            }
        }
    }

    fun deleteProduct(product: com.nathanrds.caftracker.domain.model.Product) {
        viewModelScope.launch {
            try {
                val result = deleteProductUseCase(product)
                if (result is com.nathanrds.caftracker.domain.util.Result.Error) {
                    _uiState.value = _uiState.value.copy(errorMessage = result.message)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Erro ao deletar produto: ${e.message}")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}