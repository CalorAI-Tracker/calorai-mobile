package dev.calorai.mobile.features.meal.create.manual.ui

sealed interface MealManualEditorUiEvent {
    data class NameChange(val value: String) : MealManualEditorUiEvent
    data class ProteinsChange(val value: String) : MealManualEditorUiEvent
    data class FatsChange(val value: String) : MealManualEditorUiEvent
    data class CarbsChange(val value: String) : MealManualEditorUiEvent
    data class PortionChange(val value: String) : MealManualEditorUiEvent
    data object SubmitClick : MealManualEditorUiEvent
    data object BackPressed : MealManualEditorUiEvent
}