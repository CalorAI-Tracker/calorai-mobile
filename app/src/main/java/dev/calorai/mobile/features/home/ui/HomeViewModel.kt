package dev.calorai.mobile.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.core.uikit.pieChart.PieChartUiModel
import dev.calorai.mobile.features.meal.domain.model.MealType
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel
import dev.calorai.mobile.features.home.domain.CheckIsFirstDayOfWeekUseCase
import dev.calorai.mobile.features.home.domain.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.home.domain.GetWeekByDateUseCase
import dev.calorai.mobile.features.meal.create.manual.navigateToCreateMealManualScreen
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import dev.calorai.mobile.features.home.domain.usecases.GetMealsForDayUseCase
import dev.calorai.mobile.features.home.ui.model.PieChartSubtextUi
import dev.calorai.mobile.features.meal.data.mappers.MealMapper
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
    private val mapper: MealMapper,
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
            val meals = getMealsForDayUseCase.invoke(selectedDate.toString())
                .map { dailyMeal -> mapper.mapToUiModel(dailyMeal) }
            val pieChartsData = CreatePieChartUiModels(selectedDate)
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
                val meals = getMealsForDayUseCase.invoke(nextDate.toString())
                    .map { dailyMeal -> mapper.mapToUiModel(dailyMeal) }
                val pieChartsData = CreatePieChartUiModels(nextDate)
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
                val meals = getMealsForDayUseCase.invoke(previousDate.toString())
                    .map { dailyMeal -> mapper.mapToUiModel(dailyMeal) }
                val pieChartsData = CreatePieChartUiModels(previousDate)
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

    private suspend fun CreatePieChartUiModels(
        date: LocalDate
    ) : List<PieChartUiModel> {
        // TODO: Потом поменять на получение целей
        val goalKcal: Int = 2000
        val goalProtein: Double = 100.0
        val goalFat: Double = 70.0
        val goalCarbs: Double = 225.0

        val meals = getMealsForDayUseCase.invoke(date.toString())

        val totalKcal = meals.sumOf { it.kcal }
        val totalProtein = meals.sumOf { it.proteinG.toDoubleOrNull() ?: 0.0 }
        val totalFat = meals.sumOf { it.fatG.toDoubleOrNull() ?: 0.0 }
        val totalCarbs = meals.sumOf { it.carbsG.toDoubleOrNull() ?: 0.0 }

        return listOf(
            PieChartUiModel(
                targetText = calcRemainingAmount(
                    totalKcal,
                    goalKcal
                ).toString(),
                targetSubtext = PieChartSubtextUi.KCAL.labelResId,
                leftText = "",
                pieData = calcRatios(totalKcal.toDouble(), goalKcal.toDouble()),
            ),
            PieChartUiModel(
                targetText = "${calcRemainingAmount(
                    totalProtein,
                    goalProtein
                )} г",
                targetSubtext = PieChartSubtextUi.PROTEIN.labelResId,
                leftText = "",
                pieData = calcRatios(totalProtein, goalProtein),
            ),
            PieChartUiModel(
                targetText = "${calcRemainingAmount(
                    totalFat,
                    goalFat
                )} г",
                targetSubtext = PieChartSubtextUi.FAT.labelResId,
                leftText = "",
                pieData = calcRatios(totalFat, goalFat),
            ),
            PieChartUiModel(
                targetText = "${calcRemainingAmount(
                    totalCarbs,
                    goalCarbs
                )} г",
                targetSubtext = PieChartSubtextUi.CARBS.labelResId,
                leftText = "",
                pieData = calcRatios(totalCarbs, goalCarbs),
            ),
        )
    }

    private fun calcRemainingAmount(consumed: Double, goal: Double): Double {
        val remaining = goal - consumed
        return if (remaining > 0) remaining else 0.0
    }

    private fun calcRemainingAmount(consumed: Int, goal: Int): Int {
        val remaining = goal - consumed
        return if (remaining > 0) remaining else 0
    }

    private fun calcRatios(consumed: Double, goal: Double): List<Float> {
        if (goal <= 0.0) return listOf(0f, 0f)
        val consumedRatio = (consumed / goal)
            .coerceIn(0.0, 1.0)
        return listOf(
            consumedRatio.toFloat(),
            (1.0 - consumedRatio).toFloat()
        )
    }
}

