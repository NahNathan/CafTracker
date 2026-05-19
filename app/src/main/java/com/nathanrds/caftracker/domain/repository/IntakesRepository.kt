package com.nathanrds.caftracker.domain.repository

import com.nathanrds.caftracker.domain.model.DailyTotal
import com.nathanrds.caftracker.domain.model.Intake
import kotlinx.coroutines.flow.Flow

interface IntakesRepository {
    fun getTodayIntakes(todayEpochDay: Long): Flow<List<Intake>>
    fun getTodayTotalMg(todayEpochDay: Long): Flow<Double>
    fun getLast7DaysSummary(sevenDaysAgoEpochDay: Long): Flow<List<DailyTotal>>
    suspend fun insertIntake(intake: Intake, caffeineMgPerUnitSnapshot: Double): Long
}