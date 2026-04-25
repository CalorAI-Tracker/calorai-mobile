package dev.calorai.mobile.features.meal.edit.manual.ui

import androidx.annotation.StringRes
import dev.calorai.mobile.R
import dev.calorai.mobile.features.meal.domain.model.MealType

sealed interface MealManualEditorUiState {

    val mode: MealManualEditorMode
    val mealType: MealType

    data class Ready(
        override val mode: MealManualEditorMode,
        override val mealType: MealType,
        val name: String = "",
        val proteins: String = "",
        val fats: String = "",
        val carbs: String = "",
        val portion: String = "",
    ) : MealManualEditorUiState

    data class Loading (
        override val mode: MealManualEditorMode,
        override val mealType: MealType,
    ) : MealManualEditorUiState

    @get:StringRes
    val actionButtonTextRes: Int
        get() = when (mode) {
            is MealManualEditorMode.Create -> R.string.create_meal_manual_add
            is MealManualEditorMode.Edit -> R.string.edit_meal_manual_save
        }

    @get:StringRes
    val titleTextRes: Int
        get() = when (mealType) {
            MealType.BREAKFAST -> R.string.details_meal_type_breakfast
            MealType.LUNCH -> R.string.details_meal_type_lunch
            MealType.DINNER -> R.string.details_meal_type_dinner
            MealType.SNACK -> R.string.details_meal_type_snack
        }
}
