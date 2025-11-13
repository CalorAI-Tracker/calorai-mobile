package dev.calorai.mobile.main.features.home.ui.model
import java.util.Calendar

data class WeekBarUiModel(
    val selectedDay: Int,
    val daysList: List<DateUiModel>
)

data class DateUiModel(
    val date: Calendar,
    val day: WeekDay,
    val progressFractions: List<Float>,
    val timePeriod: TimePeriod,
)

enum class WeekDay(val shortName: String) {
    MONDAY("Пн"),
    TUESDAY("Вт"),
    WEDNESDAY("Ср"),
    THURSDAY("Чт"),
    FRIDAY("Пт"),
    SATURDAY("Сб"),
    SUNDAY("Вс");
}

enum class TimePeriod(val time: String) {
    PAST("past"),
    PRESENT("present"),
    FUTURE("future")
}