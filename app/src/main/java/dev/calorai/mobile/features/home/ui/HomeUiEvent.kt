package dev.calorai.mobile.features.home.ui

import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
import dev.calorai.mobile.core.uikit.weekBar.DateUiModel

sealed interface HomeUiEvent {
    data class SelectDate(val date: DateUiModel) : HomeUiEvent
    data object SelectNextDate : HomeUiEvent
    data object SelectPreviousDate : HomeUiEvent
    data class MealCardClick(val meal: MealUiModel) : HomeUiEvent
    data class MealCardAddButtonClick(val meal: MealUiModel) : HomeUiEvent
    data object HideAddIngredientDialog : HomeUiEvent
    data object AddManualClick : HomeUiEvent
    data object ChooseReadyClick : HomeUiEvent
}
