package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealProgressInfo
import dev.calorai.mobile.features.meal.domain.model.MealType

interface GetMealProgressUseCase {
    suspend operator fun invoke(
        date: String,
        mealType: MealType,
    ): MealProgressInfo
}

internal class GetMealProgressUseCaseImpl(
    private val repository: MealRepository,
    private val getGoalParamsForMealUseCase: GetGoalParamsForMealUseCase,
) : GetMealProgressUseCase {

    override suspend fun invoke(
        date: String,
        mealType: MealType
    ): MealProgressInfo {
        val goals = getGoalParamsForMealUseCase.invoke(date, mealType)

        val entries = repository.getMealIngredients(
            date = date,
            mealType = mealType,
        )

        var totalKcal = 0f
        var totalProtein = 0f
        var totalFat = 0f
        var totalCarbs = 0f

        entries.forEach { meal ->
            totalKcal += meal.kcal
            totalProtein += meal.proteinG.toFloat()
            totalFat += meal.fatG.toFloat()
            totalCarbs += meal.carbsG.toFloat()
        }

        return MealProgressInfo(
            entries = entries,
            remainingAmountProtein = calcRemainingAmount(totalProtein, goals.goalProtein),
            remainingAmountFat = calcRemainingAmount(totalFat, goals.goalFat),
            remainingAmountCarbs = calcRemainingAmount(totalCarbs, goals.goalCarbs),
            ratioProtein = calcRatios(totalProtein, goals.goalProtein),
            ratioFat = calcRatios(totalFat, goals.goalFat),
            ratioCarbs = calcRatios(totalCarbs, goals.goalCarbs),
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
}
