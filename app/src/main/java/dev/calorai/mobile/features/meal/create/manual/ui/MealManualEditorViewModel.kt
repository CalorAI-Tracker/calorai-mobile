package dev.calorai.mobile.features.meal.create.manual.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.toRoute
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.meal.create.manual.MealManualEditorRoute
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import dev.calorai.mobile.features.meal.domain.model.MealEntryPayload
import dev.calorai.mobile.features.meal.domain.usecases.CreateMealEntryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MealManualEditorViewModel constructor(
    private val globalRouter: Router,
    savedStateHandle: SavedStateHandle,
    private val createMealEntryUseCase: CreateMealEntryUseCase,
) : ViewModel() {

    private val mealRoute = savedStateHandle.toRoute<MealManualEditorRoute>()

    private val mode: MealManualEditorMode =
        mealRoute.entryId?.let { MealManualEditorMode.Edit(it) }
            ?: MealManualEditorMode.Create

    private val _uiState = MutableStateFlow(
        MealManualEditorUiState(
            mode = mode,
            mealType = mealRoute.mealType,
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        if (mode is MealManualEditorMode.Edit) {
            loadEntry(mode.entryId)
        }
    }

    fun onEvent(event: MealManualEditorUiEvent) {
        when (event) {
            is MealManualEditorUiEvent.NameChange -> update { copy(name = event.value) }
            is MealManualEditorUiEvent.ProteinsChange -> update { copy(proteins = event.value) }
            is MealManualEditorUiEvent.FatsChange -> update { copy(fats = event.value) }
            is MealManualEditorUiEvent.CarbsChange -> update { copy(carbs = event.value) }
            is MealManualEditorUiEvent.PortionChange -> update { copy(portion = event.value) }
            MealManualEditorUiEvent.SubmitClick -> onSubmitClick()
            MealManualEditorUiEvent.BackPressed -> navigateToMealDetails()
        }
    }

    // TODO: Сделать getMealEntryByIdUseCase, вызывать здесь, после обновлять UiState
    private fun loadEntry(entryId: Long) {
    }

    private fun onSubmitClick() {
        when (mode) {
            is MealManualEditorMode.Create -> onAddClick()
            is MealManualEditorMode.Edit -> onUpdateClick(mode.entryId)
        }
    }

    private fun onUpdateClick(entryId: Long) {
        viewModelScope.launch {
            val payload = buildPayload()
            // TODO: Сделать UpdateMealEntryByIdUseCase, вызывать здесь. Пробрасывать entryId и payload
        }
    }

    private fun onAddClick() {
        // TODO: навигация или сохранение
        viewModelScope.launch {
            // TODO: mapper
            val payload = buildPayload()
            runCatching { createMealEntryUseCase(payload) }
                .onSuccess {
                    navigateToMealDetails()
            }
        }
    }

    private fun buildPayload(): MealEntryPayload =
        MealEntryPayload(
            entryName = uiState.value.name,
            meal = uiState.value.mealType,
            eatenAt = mealRoute.date,
            proteinPerBaseG = formatDoubleInput(uiState.value.proteins)
                .toDoubleOrNull() ?: 0.0,
            fatPerBaseG = formatDoubleInput(uiState.value.fats)
                .toDoubleOrNull() ?: 0.0,
            carbsPerBaseG = formatDoubleInput(uiState.value.carbs)
                .toDoubleOrNull() ?: 0.0,
            baseQuantityGrams = 100.0,
            portionQuantityGrams = formatDoubleInput(uiState.value.portion)
                .toDoubleOrNull() ?: 0.0,
        )

    private fun navigateToMealDetails() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToMealDetailsScreen(
                    mealType = mealRoute.mealType,
                    date = mealRoute.date,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<MealManualEditorRoute>(inclusive = true)
                        .build(),
                )
            }
        }
    }

    private fun update(block: MealManualEditorUiState.() -> MealManualEditorUiState) {
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