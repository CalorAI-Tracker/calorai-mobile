package dev.calorai.mobile.features.main.features.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitHorizontalDragOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.mealCard.MealCard
import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
import dev.calorai.mobile.core.uikit.pieChart.PieChart
import dev.calorai.mobile.core.uikit.pieChart.PieChartStyle
import dev.calorai.mobile.core.uikit.pieChart.PieChartUiModel
import dev.calorai.mobile.core.uikit.weekBar.DateUiModel
import dev.calorai.mobile.core.uikit.weekBar.WeekBar
import dev.calorai.mobile.core.uikit.weekBar.WeekBarUiModel
import dev.calorai.mobile.core.uikit.weekBar.toTimePeriod
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val data by viewModel.dataState.collectAsStateWithLifecycle()
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (data) {
                HomeDataUiState.Error -> Unit
                HomeDataUiState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is HomeDataUiState.HomeData -> {
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

    if (state.showAddIngredientDialog) {
        AddIngredientDialog(
            onDismissRequest = { onEvent(HomeUiEvent.HideAddIngredientDialog) },
            onAddManualClick = { onEvent(HomeUiEvent.AddManualClick) },
            onChooseReadyClick = { onEvent(HomeUiEvent.ChooseReadyClick) }
        )
    }
}

@Composable
private fun AddIngredientDialog(
    onDismissRequest: () -> Unit,
    onAddManualClick: () -> Unit,
    onChooseReadyClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.add_ingredient_dialog_title),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = {
                        onAddManualClick()
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.details_add_manual),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Button(
                    onClick = {
                        onChooseReadyClick()
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.details_choose_ready),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
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
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
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
            .height(80.dp)
            .clickable { onEvent(HomeUiEvent.MealCardClick(meal)) },
        onAddClick = { onEvent(HomeUiEvent.MealCardAddButtonClick(meal)) },
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val locale = Locale.forLanguageTag("ru")
    val today = LocalDate.of(2025, 9, 13)
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val days = (0L..6L).map { offset ->
        val date = startOfWeek.plusDays(offset)
        DateUiModel(
            date = date,
            shortDayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, locale)
                .capitalize(
                    Locale.ROOT
                ),
            timePeriod = date.toTimePeriod(),
            progress = 0.6f,
        )
    }
    CalorAiTheme {
        HomeScreen(
            state = HomeUiState(
                weekBar = WeekBarUiModel(daysList = days, selectedDate = today),
                userName = "Олег",
            ),
            data = HomeDataUiState.HomeData(
                mealsData = emptyList(),
                pieChartsData = emptyList(),
            )
        )
    }
}

fun Modifier.onSwipe(
    threshold: Float,
    onRightSwipe: () -> Unit,
    onLeftSwipe: () -> Unit,
) = pointerInput(Unit) {
    while (true) {
        awaitPointerEventScope {
            val down = awaitPointerEvent().changes.firstOrNull() ?: return@awaitPointerEventScope
            if (!down.pressed) return@awaitPointerEventScope
            var totalX = 0f
            while (true) {
                val event = awaitHorizontalDragOrCancellation(down.id) ?: break
                val dragX = event.positionChange().x
                totalX += dragX
                event.consume()
            }
            when {
                totalX > threshold -> onRightSwipe()
                totalX < -threshold -> onLeftSwipe()
            }
        }
    }
}