package dev.calorai.mobile.features.meal.details.ui

import dev.calorai.mobile.core.uikit.mealCard.MealType

data class MealDetailsUiState(
    val mealType: MealType = MealType.BREAKFAST,
    val macros: List<MacroUi> = emptyList(),
    val ingredients: List<IngredientUi> = emptyList(),
    val showAddIngredientSheet: Boolean = false
)

data class MacroUi(
    val valueText: String,
    val label: String,
    val values: List<Float>
)

data class IngredientUi(
    val title: String,
    val kcal: String,
    val weight: String
)