package dev.calorai.mobile.features.home.domain

import android.content.Context
import androidx.core.os.ConfigurationCompat
import dev.calorai.mobile.core.uikit.weekBar.DateUiModel
import dev.calorai.mobile.core.uikit.weekBar.shortDayName
import dev.calorai.mobile.core.uikit.weekBar.toTimePeriod
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

interface GetWeekByDateUseCase {
    operator fun invoke(date: LocalDate): List<DateUiModel>
}

internal class GetWeekByDateUseCaseImpl constructor(
    private val context: Context,
) : GetWeekByDateUseCase {

    override fun invoke(date: LocalDate): List<DateUiModel> {
        val locales = ConfigurationCompat.getLocales(context.resources.configuration)
        val locale = locales[0] ?: Locale.getDefault()
        val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
        val startOfWeek = date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
        return (0L..6L).map { offset ->
            val date = startOfWeek.plusDays(offset)
            DateUiModel(
                date = date,
                shortDayName = date.shortDayName(context),
                timePeriod = date.toTimePeriod(),
                progress = 0.6f,
            )
        }
    }
}
