package com.nathanrds.caftracker.domain.model

data class Intake(
    val id: Long = 0L,
    val productId: Long,
    val amount: Double,
    val timestampMillis: Long
)
