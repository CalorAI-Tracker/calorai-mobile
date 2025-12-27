package dev.calorai.mobile.core.uikit.weekBar

import java.time.LocalDate

data class WeekBarUiModel(
    val daysList: List<DateUiModel>,
    val selectedDate: LocalDate,
)

data class DateUiModel(
    val date: LocalDate,
    val timePeriod: TimePeriod,
    val progress: Float,
)

enum class TimePeriod {
    PAST,
    PRESENT,
    FUTURE
}
