package dev.calorai.mobile.features.meal.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.meal.create.manual.navigateToCreateMealManualScreen
import dev.calorai.mobile.features.meal.data.mappers.MealMapper
import dev.calorai.mobile.features.meal.details.MealDetailsRoute
import dev.calorai.mobile.features.meal.domain.usecases.GetMealIngredientsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MealDetailsViewModel constructor(
    savedStateHandle: SavedStateHandle,
    private val getMealIngredientsUseCase: GetMealIngredientsUseCase,
    private val mapper: MealMapper,
    private val globalRouter: Router,
) : ViewModel() {

    private val mealRoute = savedStateHandle.toRoute<MealDetailsRoute>()

    private val _uiState = MutableStateFlow(
        MealDetailsUiState(
            mealType = mealRoute.mealType
        )
    )
    val uiState = _uiState.asStateFlow()

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
        }
    }

    private fun openAddIngredientSheet() {
        _uiState.update {
            it.copy(showAddIngredientSheet = true)
        }
    }

    private fun closeAddIngredientSheet() {
        _uiState.update {
            it.copy(showAddIngredientSheet = false)
        }
    }

    private fun addIngredientManual() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToCreateMealManualScreen(
                    mealType = mealRoute.mealType,
                    date = mealRoute.date,
                )
            }
            _uiState.update { it.copy(showAddIngredientSheet = false) }
        }
    }

    private fun chooseReadyIngredient() {
        // здесь будет навигация на экран выбора готового
        _uiState.update { it.copy(showAddIngredientSheet = false) }
    }

    private fun continueClick() {
        // переход дальше по сценарию (сохранение, навигация и т.п.)
    }

    private fun onIngredientClick(ingredient: IngredientUi) {
        // переход в детали ингредиента / редактирование
    }

    private fun loadIngredients() {
        viewModelScope.launch {
            try {
                val ingredients = getMealIngredientsUseCase(
                    date = mealRoute.date,
                    mealType = mealRoute.mealType,
                )
                _uiState.update {
                    it.copy(
                        ingredients = ingredients.map { mapper.mapToIngredientUiModel(it) }
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(ingredients = emptyList())
                }
            }
        }
    }
}
