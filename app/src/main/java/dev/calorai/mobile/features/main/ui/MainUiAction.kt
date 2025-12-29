package dev.calorai.mobile.features.main.ui

import dev.calorai.mobile.features.meal.domain.model.MealType

sealed interface MainUiAction {
    data class ModalCreateMealButtonClick(val mealType: MealType) : MainUiAction
}
