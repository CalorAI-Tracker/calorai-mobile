package dev.calorai.mobile.features.meal.details.ui

data class MealDetailsUiState(
    val title: String = "",
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