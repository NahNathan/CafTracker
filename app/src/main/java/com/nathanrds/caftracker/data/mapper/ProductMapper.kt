package com.nathanrds.caftracker.data.mapper

import com.nathanrds.caftracker.data.room.entity.ProductEntity
import com.nathanrds.caftracker.domain.model.Product

object ProductMapper {
    fun ProductEntity.toDomain(): Product {
        return Product(
            id = id,
            name = name,
            caffeineMgPerUnit = caffeineMgPerUnit,
            unitType = unitType,
            defaultAmount = defaultAmount,
            notes = notes
        )
    }

    fun Product.toEntity(
        createdAtMillis: Long = System.currentTimeMillis(),
        updatedAtMillis: Long = System.currentTimeMillis()
    ): ProductEntity {
        return ProductEntity(
            id = id,
            name = name,
            caffeineMgPerUnit = caffeineMgPerUnit,
            unitType = unitType,
            defaultAmount = defaultAmount,
            notes = notes,
            createdAtMillis = createdAtMillis,
            updatedAtMillis = updatedAtMillis
        )
    }
}