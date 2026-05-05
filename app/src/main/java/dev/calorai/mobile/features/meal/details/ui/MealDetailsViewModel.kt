package dev.calorai.mobile.features.meal.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.toRoute
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.meal.data.mappers.MealMapper
import dev.calorai.mobile.features.meal.details.MealDetailsRoute
import dev.calorai.mobile.features.meal.domain.model.MealEntryId
import dev.calorai.mobile.features.meal.domain.model.MealProgressInfo
import dev.calorai.mobile.features.meal.domain.usecases.DeleteMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.GetMealProgressUseCase
import dev.calorai.mobile.features.meal.edit.manual.navigateToMealManualEditorScreen
import dev.calorai.mobile.features.meal.ready.MealReadyListSource
import dev.calorai.mobile.features.meal.ready.navigateToMealReadyListScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MealDetailsViewModel constructor(
    savedStateHandle: SavedStateHandle,
    private val getMealProgressUseCase: GetMealProgressUseCase,
    private val deleteMealEntryUseCase: DeleteMealEntryUseCase,
    private val mapper: MealMapper,
    private val globalRouter: Router,
) : ViewModel() {

    private val mealRoute = savedStateHandle.toRoute<MealDetailsRoute>()

    private val showAddIngredientSheetState = MutableStateFlow(false)
    private val mealProgress: MutableStateFlow<MealProgressInfo?> = MutableStateFlow(null)

    val uiState: StateFlow<MealDetailsUiState> = combine(
        mealProgress,
        showAddIngredientSheetState,
    ) { progress: MealProgressInfo?, showAddIngredientSheet ->
        if (progress == null) {
            MealDetailsUiState.Loading
        } else {
            MealDetailsUiState.Ready(
                mealType = mealRoute.mealType,
                macros = mapper.mapToMacroUiModel(progress),
                ingredients = progress.entries.map(mapper::mapToIngredientUiModel),
                showAddIngredientSheet = showAddIngredientSheet,
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        MealDetailsUiState.Loading,
    )

    init {
        loadIngredients()
    }

    fun onEvent(event: MealDetailsUiEvent) {
        when (event) {
            MealDetailsUiEvent.AddMoreClick -> openAddIngredientSheet()
            MealDetailsUiEvent.CloseAddIngredient -> closeAddIngredientSheet()
            MealDetailsUiEvent.AddManualClick -> addIngredientManual()
            MealDetailsUiEvent.ChooseReadyClick -> chooseReadyIngredient()
            MealDetailsUiEvent.ContinueClick -> continueClick()
            is MealDetailsUiEvent.IngredientClick -> onIngredientClick(event.ingredient)
            is MealDetailsUiEvent.IngredientDeleteClick -> deleteIngredient(event.ingredient.id)
        }
    }

    private fun openAddIngredientSheet() {
        showAddIngredientSheetState.update { true }
    }

    private fun closeAddIngredientSheet() {
        showAddIngredientSheetState.update { false }
    }

    private fun addIngredientManual() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToMealManualEditorScreen(
                    mealType = mealRoute.mealType,
                    date = mealRoute.date,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<MealDetailsRoute>(inclusive = true)
                        .build(),
                )
            }
            showAddIngredientSheetState.update { false }
        }
    }

    private fun deleteIngredient(entryId: Long) {
        viewModelScope.launch {
            runCatching {
                deleteMealEntryUseCase.invoke(
                    mealEntryId = MealEntryId(entryId),
                    date = mealRoute.date,
                )
            }.onSuccess {
                loadIngredients()
            }
        }
    }

    private fun chooseReadyIngredient() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToMealReadyListScreen(
                    mealType = mealRoute.mealType,
                    date = mealRoute.date,
                    source = MealReadyListSource.DETAILS,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<MealDetailsRoute>(inclusive = true)
                        .build(),
                )
            }
            showAddIngredientSheetState.update { false }
        }
    }

    private fun continueClick() {
        viewModelScope.launch {
            globalRouter.emit { popBackStack() }
        }
    }

    private fun onIngredientClick(ingredient: IngredientUi) {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToMealManualEditorScreen(
                    mealType = mealRoute.mealType,
                    date = mealRoute.date,
                    entryId = ingredient.id,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<MealDetailsRoute>(inclusive = true)
                        .build(),
                )
            }
        }
    }

    private fun loadIngredients() {
        viewModelScope.launch {
            runCatching {
                getMealProgressUseCase.invoke(
                    date = mealRoute.date,
                    mealType = mealRoute.mealType,
                )
            }
                .onSuccess { progress ->
                    if (progress.entries.isEmpty()) {
                        globalRouter.emit { popBackStack() }
                    } else {
                        mealProgress.update { progress }
                    }
                }
                .onFailure {
                    mealProgress.update { null }
                }
        }
    }
}
