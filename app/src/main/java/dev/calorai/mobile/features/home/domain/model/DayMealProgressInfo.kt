package dev.calorai.mobile.features.home.domain.model

import dev.calorai.mobile.features.meal.domain.model.DailyMeal
import java.time.LocalDate

data class DayMealProgressInfo constructor(
    val date: LocalDate,
    val meals: List<DailyMeal>,
    val remainingAmountKcal: Float,
    val remainingAmountProtein: Float,
    val remainingAmountFat: Float,
    val remainingAmountCarbs: Float,
    val ratioKcal: List<Float>,
    val ratioProtein: List<Float>,
    val ratioFat: List<Float>,
    val ratioCarbs: List<Float>,
)
