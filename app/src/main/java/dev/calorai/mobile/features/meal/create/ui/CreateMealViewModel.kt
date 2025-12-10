package dev.calorai.mobile.features.meal.create.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dev.calorai.mobile.features.meal.create.CreateMealManualRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateMealManualViewModel constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mealRoute = savedStateHandle.toRoute<CreateMealManualRoute>()
    val mealType = mealRoute.mealType

    private val _uiState = MutableStateFlow(CreateMealManualUiState())
    val uiState = _uiState.asStateFlow()


    fun onEvent(event: CreateMealManualUiEvent) {
        when (event) {
            is CreateMealManualUiEvent.NameChange ->
                update { copy(name = event.value) }

            is CreateMealManualUiEvent.CaloriesChange ->
                update { copy(calories = event.value.toDoubleOrNull() ?: calories) }

            is CreateMealManualUiEvent.ProteinsChange ->
                update { copy(proteins = event.value.toDoubleOrNull() ?: proteins) }

            is CreateMealManualUiEvent.FatsChange ->
                update { copy(fats = event.value.toDoubleOrNull() ?: fats) }

            is CreateMealManualUiEvent.CarbsChange ->
                update { copy(carbs = event.value.toDoubleOrNull() ?: carbs) }

            is CreateMealManualUiEvent.PortionChange ->
                update { copy(portion = event.value.toDoubleOrNull() ?: portion) }

            CreateMealManualUiEvent.AddClick ->
                onAddClick()
        }
    }

    private fun onAddClick() {
        // TODO: навигация или сохранение
    }


    private fun update(block: CreateMealManualUiState.() -> CreateMealManualUiState) {
        _uiState.update { it.block() }
    }
}
