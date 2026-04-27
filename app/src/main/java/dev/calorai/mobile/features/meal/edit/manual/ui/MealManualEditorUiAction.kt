package dev.calorai.mobile.features.meal.edit.manual.ui

sealed interface MealManualEditorUiAction {

    data object ShowMealNotRecognizedMessage : MealManualEditorUiAction
}
