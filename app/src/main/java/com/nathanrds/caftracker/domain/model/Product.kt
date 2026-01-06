package com.nathanrds.caftracker.domain.model

data class Product(
    val id: Long = 0L,
    val name: String,
    val caffeineMgPerUnit: Double,
    val unitType: UnitType,
    val defaultAmount: Double? = null,
    val notes: String? = null
)