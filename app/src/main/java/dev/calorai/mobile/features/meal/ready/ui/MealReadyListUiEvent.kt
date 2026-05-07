package dev.calorai.mobile.features.meal.ready.ui

sealed interface MealReadyListUiEvent {
    data object BackClick : MealReadyListUiEvent
    data class QueryChange(val value: String) : MealReadyListUiEvent
    data class MealClick(val id: String) : MealReadyListUiEvent
    data object LoadNextPage : MealReadyListUiEvent
    data object AddClick : MealReadyListUiEvent
}
