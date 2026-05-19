package com.nathanrds.caftracker.domain.usecase.intakes

import com.nathanrds.caftracker.domain.repository.IntakesRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class GetTodayTotalUseCase(
    private val repository: IntakesRepository
) {
    operator fun invoke(): Flow<Double> {
        val todayEpochDay = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
        return repository.getTodayTotalMg(todayEpochDay)
    }
}