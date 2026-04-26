package dev.calorai.mobile.features.meal.ready.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.toRoute
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.meal.domain.model.MealEntryPayload
import dev.calorai.mobile.features.meal.domain.usecases.CreateMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.GetAllMealEntriesUseCase
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import dev.calorai.mobile.features.meal.ready.MealReadyListRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MealReadyListViewModel(
    savedStateHandle: SavedStateHandle,
    private val getAllMealEntriesUseCase: GetAllMealEntriesUseCase,
    private val createMealEntryUseCase: CreateMealEntryUseCase,
    private val globalRouter: Router,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<MealReadyListRoute>()

    private val _uiState = MutableStateFlow<MealReadyListUiState>(MealReadyListUiState.Loading)
    val uiState: StateFlow<MealReadyListUiState> = _uiState.asStateFlow()

    init {
        loadMeals()
    }

    fun onBackClick() {
        viewModelScope.launch {
            globalRouter.emit { popBackStack() }
        }
    }

    fun onQueryChange(value: String) {
        _uiState.update { current ->
            when (current) {
                MealReadyListUiState.Loading -> current
                is MealReadyListUiState.Ready -> {
                    val isSelectedVisible = current.meals.any { meal ->
                        meal.id == current.selectedMealId &&
                            (value.isBlank() || meal.title.contains(value, ignoreCase = true))
                    }
                    current.copy(
                        query = value,
                        selectedMealId = current.selectedMealId.takeIf { isSelectedVisible },
                    )
                }
            }
        }
    }

    fun onMealClick(id: Long) {
        _uiState.update { current ->
            when (current) {
                MealReadyListUiState.Loading -> current
                is MealReadyListUiState.Ready -> current.copy(selectedMealId = id)
            }
        }
    }

    fun onAddClick() {
        val currentState = _uiState.value as? MealReadyListUiState.Ready ?: return
        val selectedMeal = currentState.meals.find { it.id == currentState.selectedMealId } ?: return

        viewModelScope.launch {
            runCatching {
                createMealEntryUseCase(
                    MealEntryPayload(
                        entryName = selectedMeal.title,
                        meal = route.mealType,
                        eatenAt = route.date,
                        proteinPerBaseG = selectedMeal.protein,
                        fatPerBaseG = selectedMeal.fat,
                        carbsPerBaseG = selectedMeal.carbs,
                        baseQuantityGrams = 100.0,
                        portionQuantityGrams = selectedMeal.quantityGrams,
                    )
                )
            }.onSuccess {
                globalRouter.emit {
                    navigateToMealDetailsScreen(
                        mealType = route.mealType,
                        date = route.date,
                        navOptions = NavOptions.Builder()
                            .setPopUpTo<MealReadyListRoute>(inclusive = true)
                            .build(),
                    )
                }
            }
        }
    }

    private fun loadMeals() {
        viewModelScope.launch {
            runCatching {
                getAllMealEntriesUseCase()
            }.onSuccess { entries ->
                val uniqueEntries = entries.distinctBy { entry ->
                    entry.name.trim().lowercase()
                }
                _uiState.update {
                    MealReadyListUiState.Ready(
                        meals = uniqueEntries.map { entry ->
                            ReadyMealUi(
                                id = entry.id.value,
                                title = entry.name,
                                kcal = entry.kcal,
                                protein = entry.proteinG,
                                fat = entry.fatG,
                                carbs = entry.carbsG,
                                quantityGrams = entry.quantityGrams,
                            )
                        }
                    )
                }
            }.onFailure {
                _uiState.update {
                    MealReadyListUiState.Ready(meals = emptyList())
                }
            }
        }
    }
}
