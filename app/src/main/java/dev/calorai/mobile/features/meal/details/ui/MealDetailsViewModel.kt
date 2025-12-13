package dev.calorai.mobile.features.meal.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dev.calorai.mobile.features.meal.details.MealDetailsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MealDetailsViewModel constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mealRoute = savedStateHandle.toRoute<MealDetailsRoute>()

    private val _uiState = MutableStateFlow(
        MealDetailsUiState(
            mealType = mealRoute.mealType
        )
    )
    val uiState = _uiState.asStateFlow()


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
        // TODO: mealId хз что с ним делать и как передавать
        _uiState.update {
            it.copy(showAddIngredientSheet = false)
        }
    }

    private fun chooseReadyIngredient() {
        // здесь будет навигация на экран выбора готового
        _uiState.update {
            it.copy(showAddIngredientSheet = false)
        }
    }

    private fun continueClick() {
        // переход дальше по сценарию (сохранение, навигация и т.п.)
    }

    private fun onIngredientClick(ingredient: IngredientUi) {
        // переход в детали ингредиента / редактирование
    }
}
