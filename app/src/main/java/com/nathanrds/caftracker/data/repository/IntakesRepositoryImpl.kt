package com.nathanrds.caftracker.data.repository

import com.nathanrds.caftracker.data.mapper.IntakeMapper.toDomain
import com.nathanrds.caftracker.data.mapper.IntakeMapper.toEntity
import com.nathanrds.caftracker.data.room.dao.DailyTotalRow
import com.nathanrds.caftracker.data.room.dao.IntakeDao
import com.nathanrds.caftracker.domain.model.Intake
import com.nathanrds.caftracker.domain.repository.IntakesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class IntakesRepositoryImpl(
    private val intakeDao: IntakeDao
) : IntakesRepository {

    override fun getTodayIntakes(todayEpochDay: Long): Flow<List<Intake>> {
        return intakeDao.getTodayIntakes(todayEpochDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTodayTotalMg(todayEpochDay: Long): Flow<Double> {
        return intakeDao.getTodayTotalMg(todayEpochDay)
    }

    override fun getLast7DaysSummary(sevenDaysAgoEpochDay: Long): Flow<List<com.nathanrds.caftracker.domain.model.DailyTotal>> {
        return intakeDao.getLast7DaysSummaryRaw(sevenDaysAgoEpochDay)
            .catch { exception ->
                // Em caso de erro, retornar lista vazia
                emit(emptyList())
            }
            .map { rows ->
                rows.map { com.nathanrds.caftracker.domain.model.DailyTotal(it.dateEpochDay, it.totalMg) }
            }
    }

    override suspend fun insertIntake(intake: Intake, caffeineMgPerUnitSnapshot: Double): Long {
        return intakeDao.insert(intake.toEntity(caffeineMgPerUnitSnapshot))
    }
}