package com.nathanrds.caftracker.domain.usecase.intakes

import com.nathanrds.caftracker.domain.model.DailyTotal
import com.nathanrds.caftracker.domain.repository.IntakesRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class GetLast7DaysSummaryUseCase(
    private val repository: IntakesRepository
) {
    operator fun invoke(): Flow<List<DailyTotal>> {
        val sevenDaysAgo = LocalDate.now(ZoneId.systemDefault()).minusDays(6)
        val sevenDaysAgoEpochDay = sevenDaysAgo.toEpochDay()
        return repository.getLast7DaysSummary(sevenDaysAgoEpochDay)
    }
}