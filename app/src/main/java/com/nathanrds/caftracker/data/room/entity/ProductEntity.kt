package com.nathanrds.caftracker.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nathanrds.caftracker.domain.model.UnitType

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val caffeineMgPerUnit: Double,
    val unitType: UnitType,
    val defaultAmount: Double? = null,
    val notes: String? = null,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val updatedAtMillis: Long = System.currentTimeMillis()
)
