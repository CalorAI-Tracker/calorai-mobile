package dev.calorai.mobile.main.features.home.ui.model
import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class WeekBarUiModel(
    val daysList: List<DateUiModel>
)

data class DateUiModel(
    val date: LocalDate,
    val shortDayName: String,
    val timePeriod: TimePeriod,
    val progress: Float,
    val isSelected: Boolean,
)

enum class TimePeriod {
    PAST,
    PRESENT,
    FUTURE
}

// TODO: перенести в UseCase
fun LocalDate.shortDayName(context: Context): String {
    val locales = ConfigurationCompat.getLocales(context.resources.configuration)
    val locale = locales[0] ?: Locale.getDefault()
    return this.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, locale)
}

// TODO: перенести в UseCase
fun LocalDate.toTimePeriod(referenceDate: LocalDate = LocalDate.now()): TimePeriod =
    when {
        this.isBefore(referenceDate) -> TimePeriod.PAST
        this.isAfter(referenceDate) -> TimePeriod.FUTURE
        else -> TimePeriod.PRESENT
    }