package dev.calorai.mobile.features.meal.create.manual.ui

sealed interface MealManualEditorMode {
    data object Create : MealManualEditorMode
    data class Edit(val entryId: Long) : MealManualEditorMode
}