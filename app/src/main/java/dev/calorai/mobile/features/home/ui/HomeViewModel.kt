package dev.calorai.mobile.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel
import dev.calorai.mobile.features.home.domain.CheckIsFirstDayOfWeekUseCase
import dev.calorai.mobile.features.home.domain.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.home.domain.GetMealsForDayUseCase
import dev.calorai.mobile.features.home.domain.GetPieChartsDataForDayUseCase
import dev.calorai.mobile.features.home.domain.GetWeekByDateUseCase
import dev.calorai.mobile.features.meal.create.manual.navigateToCreateMealManualScreen
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel constructor(
    private val getWeekByDateUseCase: GetWeekByDateUseCase,
    private val getCurrentUserNameUseCase: GetCurrentUserNameUseCase,
    private val checkIsFirstDayOfWeekUseCase: CheckIsFirstDayOfWeekUseCase,
    private val getMealsForDayUseCase: GetMealsForDayUseCase,
    private val getPieChartsDataForDayUseCase: GetPieChartsDataForDayUseCase,
    private val globalRouter: Router,
) : ViewModel() {

    private val _weekBarState = MutableStateFlow(LocalDate.now().let { today ->
        WeekBarUiModel(
            daysList = getWeekByDateUseCase.invoke(today),
            selectedDate = today,
        )
    })
    private val showAddIngredientDialogState = MutableStateFlow(false)

    val state: StateFlow<HomeUiState> =
        combine(
            _weekBarState,
            showAddIngredientDialogState,
            getCurrentUserNameUseCase.invoke()
        ) { week, showAddIngredientDialog, name ->
            HomeUiState.Ready(
                weekBar = week,
                userName = name,
                showAddIngredientDialog = showAddIngredientDialog,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(500L),
            HomeUiState.Loading,
        )

    private val _dataState: MutableStateFlow<HomeDataUiState> =
        MutableStateFlow(HomeDataUiState.Loading)

    val dataState: StateFlow<HomeDataUiState> = _dataState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = HomeDataUiState.Loading,
    )

    private var selectedMealId: Long? = null

    init {
        handleSelectDate(selectedDate = LocalDate.now())
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.SelectDate -> handleSelectDate(event.date.date)
            is HomeUiEvent.MealCardAddButtonClick -> handleMealCardAddButtonClick(event.meal.id)
            is HomeUiEvent.MealCardClick -> navigateToMealDetails(event.meal.type)
            HomeUiEvent.SelectNextDate -> selectNextDate()
            HomeUiEvent.SelectPreviousDate -> selectPreviousDate()
            HomeUiEvent.AddManualClick -> handleAddManualClick()
            HomeUiEvent.ChooseReadyClick -> handleChooseReadyClick()
            HomeUiEvent.HideAddIngredientDialog -> handleHideAddIngredientDialog()
        }
    }

    private fun handleSelectDate(selectedDate: LocalDate) {
        viewModelScope.launch {
            _weekBarState.update { it.copy(selectedDate = selectedDate) }
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
        val currentDate = _weekBarState.value.selectedDate
        if (currentDate == LocalDate.now()) return
        val nextDate = currentDate.plusDays(1)
        if (checkIsFirstDayOfWeekUseCase(nextDate)) {
            viewModelScope.launch {
                _weekBarState.update {
                    WeekBarUiModel(
                        daysList = getWeekByDateUseCase.invoke(nextDate),
                        selectedDate = nextDate,
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
        val currentDate = _weekBarState.value.selectedDate
        val previousDate = currentDate.minusDays(1)
        if (checkIsFirstDayOfWeekUseCase(currentDate)) {
            viewModelScope.launch {
                _weekBarState.update {
                    WeekBarUiModel(
                        daysList = getWeekByDateUseCase.invoke(previousDate),
                        selectedDate = previousDate,
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

    private fun navigateToCreateMeal(mealId: Long) {
        viewModelScope.launch { globalRouter.emit { navigateToCreateMealManualScreen(mealId) } }
    }

    private fun navigateToMealDetails(mealType: MealType) {
        viewModelScope.launch { globalRouter.emit { navigateToMealDetailsScreen(mealType) } }
    }

    private fun handleMealCardAddButtonClick(mealId: Long) {
        showAddIngredientDialogState.update { true }
        selectedMealId = mealId
    }

    private fun handleHideAddIngredientDialog() {
        showAddIngredientDialogState.update { false }
        selectedMealId = null
    }

    private fun handleAddManualClick() {
        selectedMealId?.let {
            viewModelScope.launch {
                globalRouter.emit {
                    navigateToCreateMealManualScreen(selectedMealId!!)
                }
            }
        }
        handleHideAddIngredientDialog()
    }

    private fun handleChooseReadyClick() {
        // TODO: навигация на экран выбора готового ингредиента
        handleHideAddIngredientDialog()
    }
}
