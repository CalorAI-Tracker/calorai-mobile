package dev.calorai.mobile.features.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.AddIngredientDialog
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.mealCard.MealCard
import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
import dev.calorai.mobile.core.uikit.onSwipe
import dev.calorai.mobile.core.uikit.pieChart.PieChart
import dev.calorai.mobile.core.uikit.pieChart.PieChartStyle
import dev.calorai.mobile.core.uikit.pieChart.PieChartUiModel
import dev.calorai.mobile.core.uikit.weekBar.DateUiModel
import dev.calorai.mobile.core.uikit.weekBar.TimePeriod
import dev.calorai.mobile.core.uikit.weekBar.WeekBar
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel
import dev.calorai.mobile.core.utils.ObserveAsEvents
import dev.calorai.mobile.features.main.ui.MainUiAction
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    mainUiActions: SharedFlow<MainUiAction>,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val data by viewModel.dataState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.onStart()
        }
    }

    ObserveAsEvents(mainUiActions) { action ->
        when (action) {
            is MainUiAction.ModalCreateMealButtonClick -> viewModel.onEvent(
                HomeUiEvent.ModalCreateMealButtonClick(action.mealType)
            )
        }
    }

    HomeScreen(
        state = state,
        data = data,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun HomeScreen(
    state: HomeUiState,
    data: HomeDataUiState,
    onEvent: (HomeUiEvent) -> Unit = {},
) {
    var isRefreshing by remember { mutableStateOf(false) }
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = false
            onEvent(HomeUiEvent.OnRefresh)
        },
    ) {
        when (state) {
            HomeUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            is HomeUiState.Ready -> HomeScreenReady(state, data, onEvent)
        }
    }
}

@Composable
private fun HomeScreenReady(
    state: HomeUiState.Ready,
    data: HomeDataUiState,
    onEvent: (HomeUiEvent) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .onSwipe(
                threshold = 150f,
                onRightSwipe = { onEvent(HomeUiEvent.SelectPreviousDate) },
                onLeftSwipe = { onEvent(HomeUiEvent.SelectNextDate) },
            )
    ) {
        Text(
            modifier = Modifier
                .padding(start = 20.dp, top = 30.dp),
            text = stringResource(R.string.home_welcome_title, state.userName),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(30.dp))
        WeekBar(
            weekData = state.weekBar,
            modifier = Modifier.fillMaxWidth(),
            onDateSelected = { onEvent(HomeUiEvent.SelectDate(it)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedContent(
            targetState = data,
            transitionSpec = {
                if (initialState.date == targetState.date) {
                    return@AnimatedContent fadeIn() togetherWith fadeOut()
                }
                val direction = if (initialState.date < targetState.date) {
                    SlideDirection.Left
                } else {
                    SlideDirection.Right
                }
                slideIntoContainer(towards = direction) { offsetForFullSlide ->
                    offsetForFullSlide / 2
                } togetherWith slideOutOfContainer(towards = direction)
            },
        ) {
            when (data) {
                is HomeDataUiState.Error -> Unit
                is HomeDataUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 200.dp),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(100.dp))
                    }
                }

                is HomeDataUiState.HomeData -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            PieChartsList(data.pieChartsData)
                            Spacer(modifier = Modifier.height(25.dp))
                        }
                        items(data.mealsData) { meal ->
                            MealsListItem(meal = meal, onEvent = onEvent)
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        item { Spacer(modifier = Modifier.height(30.dp)) }
                    }
                }
            }
        }
    }

    if (state.showAddIngredientDialog) {
        AddIngredientDialog(
            onDismissRequest = { onEvent(HomeUiEvent.HideAddIngredientDialog) },
            onAddManualClick = { onEvent(HomeUiEvent.AddManualClick) },
            onChooseReadyClick = { onEvent(HomeUiEvent.ChooseReadyClick) }
        )
    }
}

@Composable
private fun PieChartsList(
    pieChartsData: List<PieChartUiModel>,
) {
    if (pieChartsData.isEmpty()) return

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PieChart(
            pieChartData = pieChartsData.first(),
            configuration = PieChartStyle.LARGE
        )
        Spacer(modifier = Modifier.height(30.dp))
        val smallPieCharts = pieChartsData.drop(1)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            smallPieCharts.forEach { model ->
                PieChart(
                    pieChartData = model,
                    configuration = PieChartStyle.MEDIUM,
                )
            }
        }
    }
}

@Composable
private fun MealsListItem(
    meal: MealUiModel,
    onEvent: (HomeUiEvent) -> Unit = {},
) {
    MealCard(
        mealData = meal,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        onCardClick = { onEvent(HomeUiEvent.MealCardClick(meal)) },
        onAddClick = { onEvent(HomeUiEvent.MealCardAddButtonClick(meal)) },
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val today = LocalDate.of(2025, 9, 13)
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val days = (0L..6L).map { offset ->
        val date = startOfWeek.plusDays(offset)
        DateUiModel(
            date = date,
            timePeriod = TimePeriod.PRESENT,
            progress = 0.6f,
        )
    }
    CalorAiTheme {
        HomeScreen(
            state = HomeUiState.Ready(
                weekBar = WeekBarUiModel(daysList = days, selectedDate = today),
                userName = "Олег",
            ),
            data = HomeDataUiState.HomeData(
                mealsData = emptyList(),
                pieChartsData = emptyList(),
                date = LocalDate.now(),
            )
        )
    }
}
