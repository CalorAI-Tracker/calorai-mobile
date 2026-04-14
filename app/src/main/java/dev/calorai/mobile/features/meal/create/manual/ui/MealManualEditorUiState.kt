package dev.calorai.mobile.features.meal.create.manual.ui

import androidx.annotation.StringRes
import dev.calorai.mobile.R
import dev.calorai.mobile.features.meal.domain.model.MealType

data class MealManualEditorUiState(
    val mode: MealManualEditorMode,
    val mealType: MealType,
    val name: String = "",
    val proteins: String = "",
    val fats: String = "",
    val carbs: String = "",
    val portion: String = "",
) {
    @get:StringRes
    val actionButtonTextRes: Int
        get() = when (mode) {
            is MealManualEditorMode.Create -> R.string.create_meal_manual_add
            is MealManualEditorMode.Edit -> R.string.edit_meal_manual_save
        }
}