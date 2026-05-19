package com.nathanrds.caftracker.data.mapper

import com.nathanrds.caftracker.data.room.entity.IntakeEntity
import com.nathanrds.caftracker.domain.model.Intake
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object IntakeMapper {
    fun IntakeEntity.toDomain(): Intake {
        return Intake(
            id = id,
            productId = productId,
            amount = amount,
            timestampMillis = timestampMillis
        )
    }

    fun Intake.toEntity(caffeineMgPerUnitSnapshot: Double): IntakeEntity {
        val timestamp = if (timestampMillis > 0) timestampMillis else System.currentTimeMillis()
        val dateEpochDay = computeDateEpochDay(timestamp)
        
        return IntakeEntity(
            id = id,
            productId = productId,
            amount = amount,
            caffeineMgPerUnitSnapshot = caffeineMgPerUnitSnapshot,
            timestampMillis = timestamp,
            dateEpochDay = dateEpochDay
        )
    }

    /**
     * Converte timestampMillis para dateEpochDay usando o timezone do sistema.
     * dateEpochDay é o número de dias desde 1970-01-01, útil para agregações diárias.
     */
    fun computeDateEpochDay(timestampMillis: Long): Long {
        val localDate = Instant.ofEpochMilli(timestampMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return localDate.toEpochDay()
    }
}