package dev.calorai.mobile.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel
import dev.calorai.mobile.features.home.domain.model.DayMealProgressInfo
import dev.calorai.mobile.features.home.domain.usecases.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.home.domain.usecases.GetDayProgressUseCase
import dev.calorai.mobile.features.home.domain.usecases.GetWeekByDateUseCase
import dev.calorai.mobile.features.meal.create.manual.navigateToCreateMealManualScreen
import dev.calorai.mobile.features.meal.data.mappers.MealMapper
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import dev.calorai.mobile.features.meal.domain.model.MealType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel constructor(
    private val getWeekByDateUseCase: GetWeekByDateUseCase,
    getCurrentUserNameUseCase: GetCurrentUserNameUseCase,
    private val getDayProgressUseCase: GetDayProgressUseCase,
    private val mapper: MealMapper,
    private val globalRouter: Router,
) : ViewModel() {

    private val currentDate = MutableStateFlow(LocalDate.now())
    private val currentWeekProgress: Flow<List<DayMealProgressInfo>> =
        currentDate.map { getWeekByDateUseCase.invoke(it) }
            .distinctUntilChanged { old, new ->
                old.first() == new.first() }
            .map { days: List<LocalDate> ->
                days.map { day -> getDayProgressUseCase.invoke(day) }
            }
            .shareIn(viewModelScope, SharingStarted.Eagerly)

    private val weekBar: Flow<WeekBarUiModel> = combine(
        currentDate,
        currentWeekProgress,
    ) { selectedDate: LocalDate, weekProgress: List<DayMealProgressInfo> ->
        WeekBarUiModel(
            daysList = weekProgress.map(mapper::mapToDateUiModel),
            selectedDate = selectedDate,
        )
    }

    private val showAddIngredientDialogState = MutableStateFlow(false)

    val state: StateFlow<HomeUiState> = combine(
        weekBar,
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

    val dataState: StateFlow<HomeDataUiState> = combine(
        currentDate,
        currentWeekProgress,
    ) { selectedDate: LocalDate, weekProgress: List<DayMealProgressInfo> ->
        val currentDayInfo = weekProgress.find { it.date == selectedDate }
        if (currentDayInfo != null) {
            HomeDataUiState.HomeData(
                mealsData = currentDayInfo.meals.map(mapper::mapToMealUiModel),
                pieChartsData = mapper.mapToPieChartUiModel(currentDayInfo),
            )
        } else {
            HomeDataUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = HomeDataUiState.Loading,
    )

    private var selectedMealType: MealType? = null

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.SelectDate -> loadDataForDate(event.date.date)
            is HomeUiEvent.MealCardAddButtonClick -> handleMealCardAddButtonClick(event.meal.type)
            is HomeUiEvent.ModalCreateMealButtonClick -> handleMealCardAddButtonClick(event.mealType)
            is HomeUiEvent.MealCardClick -> navigateToMealDetails(event.meal.type)
            HomeUiEvent.SelectNextDate -> selectNextDate()
            HomeUiEvent.SelectPreviousDate -> selectPreviousDate()
            HomeUiEvent.AddManualClick -> handleAddManualClick()
            HomeUiEvent.ChooseReadyClick -> handleChooseReadyClick()
            HomeUiEvent.HideAddIngredientDialog -> hideAddIngredientDialog()
        }
    }

    private fun loadDataForDate(date: LocalDate) {
        viewModelScope.launch { currentDate.update { date } }
    }

    private fun selectNextDate() {
        val currentDate = currentDate.value
        if (currentDate == LocalDate.now()) return
        loadDataForDate(currentDate.plusDays(1))
    }

    private fun selectPreviousDate() {
        val currentDate = currentDate.value
        loadDataForDate(currentDate.minusDays(1))
    }

    private fun navigateToMealDetails(mealType: MealType) {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToMealDetailsScreen(
                    mealType = mealType,
                    date = currentDate.value.toString(),
                )
            }
        }
    }

    private fun handleMealCardAddButtonClick(mealType: MealType) {
        showAddIngredientDialogState.update { true }
        selectedMealType = mealType
    }

    private fun hideAddIngredientDialog() {
        showAddIngredientDialogState.update { false }
        selectedMealType = null
    }

    private fun handleAddManualClick() {
        viewModelScope.launch {
            selectedMealType?.let { mealType ->
                globalRouter.emit {
                    navigateToCreateMealManualScreen(
                        mealType = mealType,
                        date = currentDate.value.toString(),
                    )
                }
            }
            hideAddIngredientDialog()
        }
    }

    private fun handleChooseReadyClick() {
        // TODO: навигация на экран выбора готового ингредиента
        hideAddIngredientDialog()
    }
}
