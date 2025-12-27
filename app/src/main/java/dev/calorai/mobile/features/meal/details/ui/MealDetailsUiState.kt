package dev.calorai.mobile.features.meal.details.ui

import androidx.annotation.StringRes
import dev.calorai.mobile.features.meal.domain.model.MealType

data class MealDetailsUiState(
    val mealType: MealType = MealType.BREAKFAST,
    val macros: List<MacroUi> = emptyList(),
    val ingredients: List<IngredientUi> = emptyList(),
    val showAddIngredientSheet: Boolean = false
)

data class MacroUi(
    val value: Float,
    @StringRes val label: Int,
    val values: List<Float>
)

data class IngredientUi(
    val title: String,
    val kcal: String,
    val weight: String
)