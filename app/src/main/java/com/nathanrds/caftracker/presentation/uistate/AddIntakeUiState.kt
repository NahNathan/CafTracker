package com.nathanrds.caftracker.presentation.uistate


import com.nathanrds.caftracker.domain.model.Product

data class AddIntakeUiState(
    val products: List<Product> = emptyList(),
    val selectedProductId: Long? = null,
    val amountText: String = "",
    val computedTotalMg: Double? = null,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)
