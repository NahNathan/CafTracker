package com.nathanrds.caftracker.presentation.uistate

import com.nathanrds.caftracker.domain.model.DailyTotal

data class HomeUiState(
    val todayTotalMg: Double = 0.0,
    val todayIntakes: List<TodayIntakeItem> = emptyList(),
    val last7Days: List<DailyTotal> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
data class TodayIntakeItem(
    val intakeId: Long,
    val productName: String,
    val amount: Double,
    val unitLabel: String,
    val totalMg: Double,
    val timeLabel: String
)