package com.nathanrds.caftracker.domain.usecase.intakes

import com.nathanrds.caftracker.domain.model.Intake
import com.nathanrds.caftracker.domain.repository.IntakesRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class GetTodayIntakesUseCase(
    private val repository: IntakesRepository
) {
    operator fun invoke(): Flow<List<Intake>> {
        val todayEpochDay = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
        return repository.getTodayIntakes(todayEpochDay)
    }
}