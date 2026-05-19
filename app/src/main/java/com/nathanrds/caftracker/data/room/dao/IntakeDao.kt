package com.nathanrds.caftracker.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nathanrds.caftracker.data.room.entity.IntakeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(intake: IntakeEntity): Long

    @Query(
        """
        SELECT * FROM intakes 
        WHERE dateEpochDay = :todayEpochDay 
        ORDER BY timestampMillis DESC
        """
    )
    fun getTodayIntakes(todayEpochDay: Long): Flow<List<IntakeEntity>>

    @Query(
        """
        SELECT COALESCE(SUM(amount * caffeineMgPerUnitSnapshot), 0.0) 
        FROM intakes 
        WHERE dateEpochDay = :todayEpochDay
        """
    )
    fun getTodayTotalMg(todayEpochDay: Long): Flow<Double>

    @Query(
        """
        SELECT dateEpochDay, SUM(amount * caffeineMgPerUnitSnapshot) as totalMg
        FROM intakes
        WHERE dateEpochDay >= :sevenDaysAgoEpochDay
        GROUP BY dateEpochDay
        ORDER BY dateEpochDay DESC
        """
    )
    fun getLast7DaysSummaryRaw(sevenDaysAgoEpochDay: Long): Flow<List<DailyTotalRow>>
}

// Classe auxiliar para resultado da query Room
data class DailyTotalRow(
    val dateEpochDay: Long,
    val totalMg: Double
)