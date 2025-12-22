package dev.calorai.mobile.features.main.features.home.ui

import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
import dev.calorai.mobile.core.uikit.pieChart.PieChartUiModel
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel

data class HomeUiState constructor(
    val weekBar: WeekBarUiModel,
    val userName: String,
    val showAddIngredientDialog: Boolean = false,
)

sealed interface HomeDataUiState {
    data class HomeData(
        val mealsData: List<MealUiModel>,
        val pieChartsData: List<PieChartUiModel>,
    ) : HomeDataUiState
    data object Loading : HomeDataUiState
    data object Error : HomeDataUiState
}