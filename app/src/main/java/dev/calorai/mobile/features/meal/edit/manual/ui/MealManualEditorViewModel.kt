package dev.calorai.mobile.features.meal.edit.manual.ui

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.toRoute
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import dev.calorai.mobile.features.meal.domain.model.MealEntryId
import dev.calorai.mobile.features.meal.domain.model.MealEntryPayload
import dev.calorai.mobile.features.meal.domain.usecases.CreateMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.GetMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.RecognizeMealUseCase
import dev.calorai.mobile.features.meal.domain.usecases.UpdateMealEntryUseCase
import dev.calorai.mobile.features.meal.edit.manual.MealManualEditorRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MealManualEditorViewModel constructor(
    private val globalRouter: Router,
    savedStateHandle: SavedStateHandle,
    private val createMealEntryUseCase: CreateMealEntryUseCase,
    private val updateMealEntryUseCase: UpdateMealEntryUseCase,
    private val getMealEntryUseCase: GetMealEntryUseCase,
    private val recognizeMealUseCase: RecognizeMealUseCase,
) : ViewModel() {

    private val mealRoute = savedStateHandle.toRoute<MealManualEditorRoute>()
    private val mode: MealManualEditorMode = mealRoute.entryId
        ?.let { MealManualEditorMode.Edit(it) } ?: MealManualEditorMode.Create

    private val _uiState: MutableStateFlow<MealManualEditorUiState> =
        MutableStateFlow(
            MealManualEditorUiState.Loading(
                mode = mode,
                mealType = mealRoute.mealType,
            )
        )
    val uiState = _uiState.asStateFlow()

    init {
        when (mode) {
            is MealManualEditorMode.Edit -> setupEditMode(mode.entryId)
            is MealManualEditorMode.Create -> setupCreateMode()
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
            is MealManualEditorUiEvent.PickImage -> pickImageOnRecognize(event.uri)
        }
    }

    private fun pickImageOnRecognize(uri: Uri?) {
        viewModelScope.launch {
            runCatching {
                recognizeMealUseCase.invoke(requireNotNull(uri))
            }.onSuccess { meal ->
                update {
                    copy(
                        name = meal.name,
                        proteins = meal.proteinPer100g.toString(),
                        fats = meal.fatPer100g.toString(),
                        carbs = meal.carbsPer100g.toString(),
                    )
                }
            }
        }
    }

    private fun setupCreateMode() {
        viewModelScope.launch {
            _uiState.update {
                MealManualEditorUiState.Ready(
                    mode = mode,
                    mealType = mealRoute.mealType,
                )
            }
        }
    }

    private fun setupEditMode(entryId: Long) {
        viewModelScope.launch {
            runCatching {
                getMealEntryUseCase.invoke(MealEntryId(entryId))
            }.onSuccess { mealEntry ->
                _uiState.update {
                    MealManualEditorUiState.Ready(
                        mode = mode,
                        mealType = mealRoute.mealType,
                        name = mealEntry.name,
                        proteins = mealEntry.proteinG.toString(),
                        fats = mealEntry.fatG.toString(),
                        carbs = mealEntry.carbsG.toString(),
                        portion = mealEntry.quantityGrams.toString(),
                    )
                }
            }
        }
    }

    private fun onSubmitClick() {
        when (mode) {
            is MealManualEditorMode.Create -> onAddClick()
            is MealManualEditorMode.Edit -> onUpdateClick(mode.entryId)
        }
    }

    private fun onUpdateClick(entryId: Long) {
        viewModelScope.launch {
            // TODO: mapper
            val payload = buildPayload(_uiState.value as MealManualEditorUiState.Ready)
            runCatching {
                updateMealEntryUseCase.invoke(
                    mealEntryId = MealEntryId(entryId),
                    payload = payload,
                )
            }.onSuccess {
                navigateToMealDetails()
            }
        }
    }

    private fun onAddClick() {
        // TODO: навигация или сохранение
        viewModelScope.launch {
            // TODO: mapper
            val payload = buildPayload(_uiState.value as MealManualEditorUiState.Ready)
            runCatching {
                createMealEntryUseCase(payload)
            }.onSuccess {
                navigateToMealDetails()
            }
        }
    }

    private fun buildPayload(uiState: MealManualEditorUiState.Ready): MealEntryPayload =
        MealEntryPayload(
            entryName = uiState.name,
            meal = uiState.mealType,
            eatenAt = mealRoute.date,
            proteinPerBaseG = formatDoubleInput(uiState.proteins)
                .toDoubleOrNull() ?: 0.0,
            fatPerBaseG = formatDoubleInput(uiState.fats)
                .toDoubleOrNull() ?: 0.0,
            carbsPerBaseG = formatDoubleInput(uiState.carbs)
                .toDoubleOrNull() ?: 0.0,
            baseQuantityGrams = 100.0,
            portionQuantityGrams = formatDoubleInput(uiState.portion)
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

    private fun update(block: MealManualEditorUiState.Ready.() -> MealManualEditorUiState.Ready) {
        _uiState.update { currentState ->
            if (currentState is MealManualEditorUiState.Ready) {
                currentState.block()
            } else {
                MealManualEditorUiState.Ready(mode = mode, mealType = mealRoute.mealType).block()
            }
        }
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
