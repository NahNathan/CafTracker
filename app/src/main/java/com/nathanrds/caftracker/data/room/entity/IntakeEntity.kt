package com.nathanrds.caftracker.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "intakes",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("productId"),
        Index("timestampMillis"),
        Index("dateEpochDay")
    ]
)
data class IntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val productId: Long,
    val amount: Double,
    val caffeineMgPerUnitSnapshot: Double,
    val timestampMillis: Long,
    val dateEpochDay: Long
)
