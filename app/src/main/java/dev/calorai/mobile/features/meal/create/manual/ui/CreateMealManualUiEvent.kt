package dev.calorai.mobile.features.meal.create.manual.ui

sealed interface CreateMealManualUiEvent {
    data class NameChange(val value: String) : CreateMealManualUiEvent
    data class ProteinsChange(val value: String) : CreateMealManualUiEvent
    data class FatsChange(val value: String) : CreateMealManualUiEvent
    data class CarbsChange(val value: String) : CreateMealManualUiEvent
    data class PortionChange(val value: String) : CreateMealManualUiEvent
    data object AddClick : CreateMealManualUiEvent
    data object BackPressed : CreateMealManualUiEvent
}