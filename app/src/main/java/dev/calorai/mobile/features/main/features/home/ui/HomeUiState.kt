package dev.calorai.mobile.features.main.features.home.ui

import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel

data class HomeUiState constructor(
    val weekBar: WeekBarUiModel,
    val userName: String,
)

sealed interface HomeMealsUiState {
    data class MealData(val data: List<MealUiModel>) : HomeMealsUiState
    data object Loading : HomeMealsUiState
    data object Error : HomeMealsUiState
}
