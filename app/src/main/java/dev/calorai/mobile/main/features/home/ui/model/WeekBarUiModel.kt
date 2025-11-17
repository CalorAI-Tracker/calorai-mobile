package dev.calorai.mobile.main.features.home.ui.model
import java.time.LocalDate

data class WeekBarUiModel(
    val daysList: List<DateUiModel>
)

data class DateUiModel(
    val date: LocalDate,
    val progressFractions: List<Float>,
    val isSelected: Boolean,
)

enum class TimePeriod() {
    PAST(),
    PRESENT(),
    FUTURE()
}
