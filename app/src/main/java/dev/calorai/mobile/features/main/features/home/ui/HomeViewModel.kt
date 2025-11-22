package dev.calorai.mobile.features.main.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentWeekUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetMealsForDayUseCase
import dev.calorai.mobile.features.meal.create.navigateToCreateMealScreen
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel constructor(
    getCurrentWeekUseCase: GetCurrentWeekUseCase,
    getCurrentUserNameUseCase: GetCurrentUserNameUseCase,
    private val getMealsForDayUseCase: GetMealsForDayUseCase,
    private val globalRouter: Router,
) : ViewModel() {

    private val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(
            weekBar = WeekBarUiModel(daysList = getCurrentWeekUseCase.invoke()),
            userName = getCurrentUserNameUseCase.invoke(),
        )
    )

    val state: StateFlow<HomeUiState> = _state

    private val _mealsState: MutableStateFlow<HomeMealsUiState> =
        MutableStateFlow(HomeMealsUiState.Loading)

    val mealsState: StateFlow<HomeMealsUiState> = _mealsState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = HomeMealsUiState.Loading,
    )

    init {
        handleSelectDate(selectedDate = LocalDate.now())
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.SelectDate -> handleSelectDate(event.date.date)
            is HomeUiEvent.MealCardAddButtonClick -> navigateToCreateMeal(event.meal.type)
            is HomeUiEvent.MealCardClick -> navigateToMealDetails(event.meal.id)
        }
    }

    private fun handleSelectDate(selectedDate: LocalDate) {
        viewModelScope.launch {
            _state.update { previousState ->
                previousState.copy(
                    weekBar = WeekBarUiModel(
                        daysList = previousState.weekBar.daysList.map { day ->
                            day.copy(isSelected = day.date == selectedDate)
                        }
                    )
                )
            }
        }
        viewModelScope.launch {
            _mealsState.update { HomeMealsUiState.Loading }
            val meals = getMealsForDayUseCase.invoke(selectedDate)
            _mealsState.update { HomeMealsUiState.MealData(meals) }
        }
    }

    private fun navigateToCreateMeal(mealType: MealType) {
        viewModelScope.launch { globalRouter.emit { navigateToCreateMealScreen(mealType) } }
    }

    private fun navigateToMealDetails(mealId: Long) {
        viewModelScope.launch { globalRouter.emit { navigateToMealDetailsScreen(mealId) } }
    }
}
