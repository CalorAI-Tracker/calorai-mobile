package dev.calorai.mobile.features.meal.edit.manual.ui

sealed interface MealManualEditorMode {
    data object Create : MealManualEditorMode
    data class Edit(val entryId: Long) : MealManualEditorMode
}
