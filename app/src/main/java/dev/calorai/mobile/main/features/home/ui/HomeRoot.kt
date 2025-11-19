package dev.calorai.mobile.main.features.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.calorai.mobile.R
import dev.calorai.mobile.main.features.home.ui.model.DateUiModel
import dev.calorai.mobile.main.features.home.ui.model.WeekBarUiModel
import dev.calorai.mobile.main.features.home.ui.model.toTimePeriod
import dev.calorai.mobile.main.features.home.ui.views.weekBar.WeekBar
import dev.calorai.mobile.ui.theme.CalorAiTheme
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
    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "HomeScreen for ${state.weekBar.daysList.find { it.isSelected }?.date}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
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
            isSelected = date == today
        )
    }
    CalorAiTheme {
        HomeScreen(
            state = HomeUiState(
                weekBar = WeekBarUiModel(daysList = days),
                userName = "Олег",
            ),
            onEvent = {},
        )
    }
}
