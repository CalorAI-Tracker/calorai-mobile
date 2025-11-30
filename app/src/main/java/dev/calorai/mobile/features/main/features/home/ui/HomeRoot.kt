package dev.calorai.mobile.features.main.features.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.mealCard.MealCard
import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
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
    val meals by viewModel.mealsState.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        meals = meals,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun HomeScreen(
    state: HomeUiState,
    meals: HomeMealsUiState,
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
            text = stringResource(R.string.home_welcome_title, state.userName),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(30.dp))
        WeekBar(
            weekData = state.weekBar,
            modifier = Modifier.fillMaxWidth(),
            onDateSelected = { onEvent(HomeUiEvent.SelectDate(it)) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            when (meals) {
                HomeMealsUiState.Error -> Unit
                HomeMealsUiState.Loading -> CircularProgressIndicator()
                is HomeMealsUiState.MealData -> MealsList(
                    meals = meals.data,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun MealsList(
    meals: List<MealUiModel>,
    onEvent: (HomeUiEvent) -> Unit = {},
) {
    LazyColumn {
        items(meals) { meal ->
            MealCard(
                mealData = meal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clickable { onEvent(HomeUiEvent.MealCardClick(meal)) },
                onAddClick = { onEvent(HomeUiEvent.MealCardAddButtonClick(meal)) },
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
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
            meals = HomeMealsUiState.MealData(emptyList()),
        )
    }
}

private fun Modifier.onSwipe(
    threshold: Float,
    onRightSwipe: () -> Unit,
    onLeftSwipe: () -> Unit,
) = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            awaitPointerEvent().changes.firstOrNull() ?: continue
            var totalX = 0f
            while (true) {
                val event = awaitPointerEvent()
                val drag = event.changes.firstOrNull() ?: break
                val changeX = drag.positionChange().x
                totalX += changeX
                drag.consume()
                if (!drag.pressed) break
            }
            when {
                totalX > threshold -> onRightSwipe()
                totalX < -threshold -> onLeftSwipe()
            }
        }
    }
}
