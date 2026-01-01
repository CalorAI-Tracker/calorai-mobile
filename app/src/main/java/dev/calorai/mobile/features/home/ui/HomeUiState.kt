package dev.calorai.mobile.features.home.ui

import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
import dev.calorai.mobile.core.uikit.pieChart.PieChartUiModel
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel
import java.time.LocalDate

sealed interface HomeUiState {
    data class Ready(
        val weekBar: WeekBarUiModel,
        val userName: String,
        val showAddIngredientDialog: Boolean = false,
    ) : HomeUiState

    data object Loading : HomeUiState
}

sealed interface HomeDataUiState {

    val date: LocalDate

    data class HomeData(
        override val date: LocalDate,
        val mealsData: List<MealUiModel>,
        val pieChartsData: List<PieChartUiModel>,
    ) : HomeDataUiState

    data class Loading(override val date: LocalDate) : HomeDataUiState
    data class Error(override val date: LocalDate) : HomeDataUiState
}