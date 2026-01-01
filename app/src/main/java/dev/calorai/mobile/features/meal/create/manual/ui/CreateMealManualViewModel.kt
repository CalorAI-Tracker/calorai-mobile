package dev.calorai.mobile.features.meal.create.manual.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.toRoute
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.meal.create.manual.CreateMealManualRoute
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import dev.calorai.mobile.features.meal.domain.model.CreateMealEntryPayload
import dev.calorai.mobile.features.meal.domain.usecases.CreateMealEntryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateMealManualViewModel constructor(
    private val globalRouter: Router,
    savedStateHandle: SavedStateHandle,
    private val createMealEntryUseCase: CreateMealEntryUseCase,
) : ViewModel() {

    private val mealRoute = savedStateHandle.toRoute<CreateMealManualRoute>()

    private val _uiState = MutableStateFlow(
        CreateMealManualUiState(
            mealType = mealRoute.mealType,
        )
    )
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: CreateMealManualUiEvent) {
        when (event) {
            is CreateMealManualUiEvent.NameChange -> update { copy(name = event.value) }
            is CreateMealManualUiEvent.ProteinsChange -> update { copy(proteins = event.value) }
            is CreateMealManualUiEvent.FatsChange -> update { copy(fats = event.value) }
            is CreateMealManualUiEvent.CarbsChange -> update { copy(carbs = event.value) }
            is CreateMealManualUiEvent.PortionChange -> update { copy(portion = event.value) }
            CreateMealManualUiEvent.AddClick -> onAddClick()
            CreateMealManualUiEvent.BackPressed -> navigateToMealDetails()
        }
    }

    private fun onAddClick() {
        // TODO: навигация или сохранение
        viewModelScope.launch {
            // TODO: mapper
            val payload = CreateMealEntryPayload(
                entryName = uiState.value.name,
                meal = mealRoute.mealType,
                eatenAt = mealRoute.date,
                proteinPerBaseG = formatDoubleInput(uiState.value.proteins)
                    .toDoubleOrNull() ?: 0.0,
                fatPerBaseG = formatDoubleInput(uiState.value.fats)
                    .toDoubleOrNull() ?: 0.0,
                carbsPerBaseG = formatDoubleInput(uiState.value.carbs)
                    .toDoubleOrNull() ?: 0.0,
                baseQuantityGrams = 100.0, // TODO: Возможно, поменять потом. Это на сколько грамм приходится стата БЖУ. Обычно, это 100 г 
                portionQuantityGrams = formatDoubleInput(uiState.value.portion)
                    .toDoubleOrNull() ?: 0.0,
            )
            runCatching { createMealEntryUseCase(payload) }
                .onSuccess {
                    navigateToMealDetails()
            }
        }
    }

    private fun navigateToMealDetails() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToMealDetailsScreen(
                    mealType = mealRoute.mealType,
                    date = mealRoute.date,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<CreateMealManualRoute>(inclusive = true)
                        .build(),
                )
            }
        }
    }

    private fun update(block: CreateMealManualUiState.() -> CreateMealManualUiState) {
        _uiState.update { it.block() }
    }
}

fun formatDoubleInput(input: String): String {
    val number = input
        .replace(',', '.')
        .toDoubleOrNull()
        ?: return "0.0"

    return if (number % 1.0 == 0.0) {
        number.toInt().toString() // 12.0 → "12"
    } else {
        number.toString().trimEnd('0').trimEnd('.') // 15.500 → "15.5"
    }
}