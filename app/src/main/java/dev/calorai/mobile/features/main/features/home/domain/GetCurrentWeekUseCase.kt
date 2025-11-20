package dev.calorai.mobile.features.main.features.home.domain

import android.content.Context
import dev.calorai.mobile.core.uikit.weekBar.DateUiModel
import dev.calorai.mobile.core.uikit.weekBar.shortDayName
import dev.calorai.mobile.core.uikit.weekBar.toTimePeriod
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

interface GetCurrentWeekUseCase {
    operator fun invoke(): List<DateUiModel>
}

internal class GetCurrentWeekUseCaseImpl constructor(
    private val context: Context,
    private val clock: Clock,
) : GetCurrentWeekUseCase {

    override fun invoke(): List<DateUiModel> {
        val today = LocalDate.now(clock)
        val locale = Locale.getDefault()
        val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
        return (0L..6L).map { offset ->
            val date = startOfWeek.plusDays(offset)
            DateUiModel(
                date = date,
                shortDayName = date.shortDayName(context),
                timePeriod = date.toTimePeriod(),
                progress = 0.6f,
                isSelected = date == today,
            )
        }
    }
}
