package dev.calorai.mobile.features.meal.create.manual.ui

import dev.calorai.mobile.features.meal.domain.model.MealType

data class CreateMealManualUiState(
    val mealType: MealType,
    val name: String = "",
    val proteins: String = "",
    val fats: String = "",
    val carbs: String = "",
    val portion: String = "",
)