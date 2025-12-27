package dev.calorai.mobile.features.home.domain.usecases

import android.content.Context
import dev.calorai.mobile.core.utils.locale
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

interface GetWeekByDateUseCase {
    operator fun invoke(date: LocalDate): List<LocalDate>
}

internal class GetWeekByDateUseCaseImpl constructor(
    private val context: Context,
) : GetWeekByDateUseCase {

    override fun invoke(date: LocalDate): List<LocalDate> {
        val firstDayOfWeek = WeekFields.of(context.locale).firstDayOfWeek
        val startOfWeek = date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
        return (0L..6L).map { offset -> startOfWeek.plusDays(offset) }
    }
}
