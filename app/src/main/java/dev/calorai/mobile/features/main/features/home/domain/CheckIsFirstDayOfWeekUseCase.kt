package dev.calorai.mobile.features.main.features.home.domain

import android.content.Context
import androidx.core.os.ConfigurationCompat
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

interface CheckIsFirstDayOfWeekUseCase {
    operator fun invoke(date: LocalDate): Boolean
}

internal class CheckIsFirstDayOfWeekUseCaseImpl constructor(
    private val context: Context,
) : CheckIsFirstDayOfWeekUseCase {

    override fun invoke(date: LocalDate): Boolean {
        val locales = ConfigurationCompat.getLocales(context.resources.configuration)
        val locale = locales[0] ?: Locale.getDefault()
        val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
        return date.dayOfWeek == firstDayOfWeek
    }
}
