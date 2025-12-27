package dev.calorai.mobile.features.home.domain.usecases

import dev.calorai.mobile.features.home.domain.model.DayMealProgressInfo
import dev.calorai.mobile.features.meal.domain.MealRepository
import java.time.LocalDate

interface GetDayProgressUseCase {
    suspend operator fun invoke(date: LocalDate): DayMealProgressInfo
}

internal class GetDayProgressUseCaseImpl(
    private val repository: MealRepository
) : GetDayProgressUseCase {

    override suspend fun invoke(date: LocalDate): DayMealProgressInfo {
        // TODO() fetch actual goal params
        val goalKcal = 2000f
        val goalProtein = 100.0f
        val goalFat = 70.0f
        val goalCarbs = 225.0f

        val meals = repository.getDailyMeals(date.toString())

        var totalKcal = 0f
        var totalProtein = 0f
        var totalFat = 0f
        var totalCarbs = 0f

        meals.forEach { meal ->
            totalKcal += meal.kcal
            totalProtein += meal.proteinG.toFloatOrZero()
            totalFat += meal.fatG.toFloatOrZero()
            totalCarbs += meal.carbsG.toFloatOrZero()
        }

        return DayMealProgressInfo(
            date = date,
            meals = meals,
            remainingAmountKcal = calcRemainingAmount(totalKcal, goalKcal),
            remainingAmountProtein = calcRemainingAmount(totalProtein, goalProtein),
            remainingAmountFat = calcRemainingAmount(totalFat, goalFat),
            remainingAmountCarbs = calcRemainingAmount(totalCarbs, goalCarbs),
            ratioKcal = calcRatios(totalKcal, goalKcal),
            ratioProtein = calcRatios(totalProtein, goalProtein),
            ratioFat = calcRatios(totalFat, goalFat),
            ratioCarbs = calcRatios(totalCarbs, goalCarbs),
        )
    }

    private fun calcRemainingAmount(consumed: Float, goal: Float): Float {
        val remaining = goal - consumed
        return if (remaining > 0) remaining else 0.0f
    }

    private fun calcRatios(consumed: Float, goal: Float): List<Float> {
        if (goal <= 0.0) return listOf(0f, 0f)
        val consumedRatio = (consumed / goal).coerceIn(0.0f, 1.0f)
        return listOf(consumedRatio, 1.0f - consumedRatio)
    }

    private fun String.toFloatOrZero(): Float = this.toFloatOrNull() ?: 0.0f
}
