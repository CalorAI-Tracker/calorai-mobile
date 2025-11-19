package dev.calorai.mobile.main.features.home.ui

import androidx.lifecycle.ViewModel
import dev.calorai.mobile.main.features.home.domain.GetCurrentUserNameUseCase
import dev.calorai.mobile.main.features.home.domain.GetCurrentWeekUseCase
import dev.calorai.mobile.main.features.home.ui.model.DateUiModel
import dev.calorai.mobile.main.features.home.ui.model.WeekBarUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel constructor(
    getCurrentWeekUseCase: GetCurrentWeekUseCase,
    getCurrentUserNameUseCase: GetCurrentUserNameUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(
            weekBar = WeekBarUiModel(daysList = getCurrentWeekUseCase.invoke()),
            userName = getCurrentUserNameUseCase.invoke(),
        )
    )

    val state: StateFlow<HomeUiState> = _state

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.SelectDate -> handleSelectDate(event.date)
        }
    }

    private fun handleSelectDate(selectedDate: DateUiModel) {
        _state.update { previousState ->
            previousState.copy(
                weekBar = WeekBarUiModel(
                    daysList = previousState.weekBar.daysList.map { day ->
                        day.copy(
                            isSelected = day.date == selectedDate.date,
                        )
                    }
                )
            )
        }
    }
}
