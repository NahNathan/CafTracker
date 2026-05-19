package com.nathanrds.caftracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nathanrds.caftracker.domain.repository.ProductsRepository
import com.nathanrds.caftracker.domain.usecase.intakes.GetLast7DaysSummaryUseCase
import com.nathanrds.caftracker.domain.usecase.intakes.GetTodayIntakesUseCase
import com.nathanrds.caftracker.domain.usecase.intakes.GetTodayTotalUseCase
import com.nathanrds.caftracker.presentation.uistate.HomeUiState
import com.nathanrds.caftracker.presentation.uistate.TodayIntakeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(
    private val getTodayTotalUseCase: GetTodayTotalUseCase,
    private val getTodayIntakesUseCase: GetTodayIntakesUseCase,
    private val getLast7DaysSummaryUseCase: GetLast7DaysSummaryUseCase,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val productsFlow = productsRepository.getAllProducts()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val uiState: StateFlow<HomeUiState> = combine(
        getTodayTotalUseCase(),
        getTodayIntakesUseCase(),
        getLast7DaysSummaryUseCase(),
        productsFlow
    ) { todayTotal, todayIntakes, last7Days, products ->
        try {
            val productsMap = products.associateBy { it.id }
            val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            
            val todayIntakeItems = todayIntakes.map { intake ->
                val product = productsMap[intake.productId]
                TodayIntakeItem(
                    intakeId = intake.id,
                    productName = product?.name ?: "Produto desconhecido",
                    amount = intake.amount,
                    unitLabel = product?.unitType?.name ?: "",
                    totalMg = intake.amount * (product?.caffeineMgPerUnit ?: 0.0),
                    timeLabel = timeFormatter.format(java.util.Date(intake.timestampMillis))
                )
            }
            
            HomeUiState(
                todayTotalMg = todayTotal,
                todayIntakes = todayIntakeItems,
                last7Days = last7Days,
                isLoading = false
            )
        } catch (e: Exception) {
            // Em caso de erro, retornar estado com erro
            HomeUiState(
                todayTotalMg = 0.0,
                todayIntakes = emptyList(),
                last7Days = emptyList(),
                isLoading = false,
                errorMessage = "Erro ao carregar dados: ${e.message}"
            )
        }
    }.catch { exception ->
        // Em caso de erro no flow, emitir estado de erro
        emit(
            HomeUiState(
                todayTotalMg = 0.0,
                todayIntakes = emptyList(),
                last7Days = emptyList(),
                isLoading = false,
                errorMessage = "Erro ao carregar dados: ${exception.message}"
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun refresh() {
        // Os flows são reativos, então não precisa fazer nada especial
    }
}