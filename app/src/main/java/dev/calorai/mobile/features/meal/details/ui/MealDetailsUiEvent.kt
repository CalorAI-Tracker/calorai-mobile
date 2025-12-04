package dev.calorai.mobile.features.meal.details.ui

sealed interface MealDetailsUiEvent {

    object AddMoreClick : MealDetailsUiEvent
    object CloseAddIngredient : MealDetailsUiEvent
    object AddManualClick : MealDetailsUiEvent
    object ChooseReadyClick : MealDetailsUiEvent
    object ContinueClick : MealDetailsUiEvent

    data class IngredientClick(
        val ingredient: IngredientUi
    ) : MealDetailsUiEvent
}