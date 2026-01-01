package dev.calorai.mobile.features.meal.details.ui

import androidx.annotation.StringRes
import dev.calorai.mobile.core.uikit.pieChart.UnitOfMeasure
import dev.calorai.mobile.features.meal.domain.model.MealType

sealed interface MealDetailsUiState {
    data class Ready(
        val mealType: MealType = MealType.BREAKFAST,
        val macros: List<MacroUi> = emptyList(),
        val ingredients: List<IngredientUi> = emptyList(),
        val showAddIngredientSheet: Boolean = false,
    ) : MealDetailsUiState

    data object Loading : MealDetailsUiState
}

data class MacroUi(
    val value: Float,
    @StringRes val label: Int,
    val values: List<Float>,
)

data class IngredientUi(
    val title: String,
    val kcal: Int,
    val weight: Double,
    val unitOfMeasure: UnitOfMeasure
)