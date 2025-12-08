package dev.calorai.mobile.features.main.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel
import dev.calorai.mobile.features.main.features.home.domain.CheckIsFirstDayOfWeekUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetWeekByDateUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetMealsForDayUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetPieChartsDataForDayUseCase
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
    private val getWeekByDateUseCase: GetWeekByDateUseCase,
    getCurrentUserNameUseCase: GetCurrentUserNameUseCase,
    private val checkIsFirstDayOfWeekUseCase: CheckIsFirstDayOfWeekUseCase,
    private val getMealsForDayUseCase: GetMealsForDayUseCase,
    private val getPieChartsDataForDayUseCase: GetPieChartsDataForDayUseCase,
    private val globalRouter: Router,
) : ViewModel() {

    private val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(
            weekBar = LocalDate.now().let { today ->
                WeekBarUiModel(
                    daysList = getWeekByDateUseCase.invoke(today),
                    selectedDate = today,
                )
            },
            userName = getCurrentUserNameUseCase.invoke(),
        )
    )

    val state: StateFlow<HomeUiState> = _state

    private val _dataState: MutableStateFlow<HomeDataUiState> =
        MutableStateFlow(HomeDataUiState.Loading)

    val dataState: StateFlow<HomeDataUiState> = _dataState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = HomeDataUiState.Loading,
    )

    init {
        handleSelectDate(selectedDate = LocalDate.now())
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.SelectDate -> handleSelectDate(event.date.date)
            is HomeUiEvent.MealCardAddButtonClick -> navigateToCreateMeal(event.meal.type)
            is HomeUiEvent.MealCardClick -> navigateToMealDetails(event.meal.id)
            HomeUiEvent.SelectNextDate -> selectNextDate()
            HomeUiEvent.SelectPreviousDate -> selectPreviousDate()
        }
    }

    private fun handleSelectDate(selectedDate: LocalDate) {
        viewModelScope.launch {
            _state.update { previousState ->
                previousState.copy(
                    weekBar = previousState.weekBar.copy(selectedDate = selectedDate)
                )
            }
        }
        viewModelScope.launch {
            _dataState.update { HomeDataUiState.Loading }
            val meals = getMealsForDayUseCase.invoke(selectedDate)
            val pieChartsData = getPieChartsDataForDayUseCase.invoke(selectedDate)
            _dataState.update {
                HomeDataUiState.HomeData(
                mealsData = meals,
                pieChartsData = pieChartsData
                )
            }
        }
    }

    private fun selectNextDate() {
        val currentDate = _state.value.weekBar.selectedDate
        if (currentDate == LocalDate.now()) return
        val nextDate = currentDate.plusDays(1)
        if (checkIsFirstDayOfWeekUseCase(nextDate)) {
            viewModelScope.launch {
                _state.update { previousState ->
                    previousState.copy(
                        weekBar = WeekBarUiModel(
                            daysList = getWeekByDateUseCase.invoke(nextDate),
                            selectedDate = nextDate,
                        )
                    )
                }
            }
            viewModelScope.launch {
                _dataState.update { HomeDataUiState.Loading }
                val meals = getMealsForDayUseCase.invoke(nextDate)
                val pieChartsData = getPieChartsDataForDayUseCase.invoke(nextDate)
                _dataState.update {
                    HomeDataUiState.HomeData(
                        mealsData = meals,
                        pieChartsData = pieChartsData
                    )
                }
            }
        } else {
            handleSelectDate(nextDate)
        }
    }

    private fun selectPreviousDate() {
        val currentDate = _state.value.weekBar.selectedDate
        val previousDate = currentDate.minusDays(1)
        if (checkIsFirstDayOfWeekUseCase(currentDate)) {
            viewModelScope.launch {
                _state.update { previousState ->
                    previousState.copy(
                        weekBar = WeekBarUiModel(
                            daysList = getWeekByDateUseCase.invoke(previousDate),
                            selectedDate = previousDate,
                        )
                    )
                }
            }
            viewModelScope.launch {
                _dataState.update { HomeDataUiState.Loading }
                val meals = getMealsForDayUseCase.invoke(previousDate)
                val pieChartsData = getPieChartsDataForDayUseCase.invoke(previousDate)
                _dataState.update {
                    HomeDataUiState.HomeData(
                        mealsData = meals,
                        pieChartsData = pieChartsData
                    )
                }
            }
        } else {
            handleSelectDate(previousDate)
        }
    }

    private fun navigateToCreateMeal(mealType: MealType) {
        viewModelScope.launch { globalRouter.emit { navigateToCreateMealScreen(mealType) } }
    }

    private fun navigateToMealDetails(mealId: Long) {
        viewModelScope.launch { globalRouter.emit { navigateToMealDetailsScreen(mealId) } }
    }
}
