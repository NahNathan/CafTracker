package com.nathanrds.caftracker.presentation.uistate
import com.nathanrds.caftracker.domain.model.UnitType

data class AddEditProductUiState(
    val productId: Long? = null,
    val name: String = "",
    val caffeineMgPerUnitText: String = "",
    val unitType: UnitType = UnitType.ML,
    val defaultAmountText: String = "",
    val notes: String = "",
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)
