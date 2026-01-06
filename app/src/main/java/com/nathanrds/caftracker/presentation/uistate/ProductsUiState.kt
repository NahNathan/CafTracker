package com.nathanrds.caftracker.presentation.uistate

import com.nathanrds.caftracker.domain.model.Product

data class ProductsUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
