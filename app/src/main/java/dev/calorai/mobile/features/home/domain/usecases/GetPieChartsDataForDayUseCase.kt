package dev.calorai.mobile.features.home.domain.usecases

import dev.calorai.mobile.core.uikit.pieChart.PieChartUiModel
import dev.calorai.mobile.features.meal.domain.MealRepository
import java.time.LocalDate

interface GetPieChartsDataForDayUseCase {
    suspend operator fun invoke(date: LocalDate): List<PieChartUiModel>
}

internal class GetPieChartsDataForDayUseCaseImpl(
    private val repository: MealRepository
) : GetPieChartsDataForDayUseCase {

    override suspend fun invoke(date: LocalDate): List<PieChartUiModel> {

        // TODO: Потом поменять на получение целей 
        val goalKcal: Int = 2000
        val goalProtein: Double = 100.0
        val goalFat: Double = 70.0
        val goalCarbs: Double = 225.0

        val dailyMeals = repository.getDailyMeals(date.toString())

        val totalKcal = dailyMeals.sumOf { it.kcal }
        val totalProtein = dailyMeals.sumOf { it.proteinG.toDoubleOrNull() ?: 0.0 }
        val totalFat = dailyMeals.sumOf { it.fatG.toDoubleOrNull() ?: 0.0 }
        val totalCarbs = dailyMeals.sumOf { it.carbsG.toDoubleOrNull() ?: 0.0 }

        return listOf(
            PieChartUiModel(
                targetText = goalKcal.toString(),
                targetSubtext = "Калорий осталось",
                leftText = "",
                pieData = ratios(totalKcal.toDouble(), goalKcal.toDouble()),
            ),
            PieChartUiModel(
                targetText = "${goalProtein.toInt()}G",
                targetSubtext = "Белка осталось",
                leftText = "",
                pieData = ratios(totalProtein, goalProtein),
            ),
            PieChartUiModel(
                targetText = "${goalFat.toInt()}G",
                targetSubtext = "Жиров осталось",
                leftText = "",
                pieData = ratios(totalFat, goalFat),
            ),
            PieChartUiModel(
                targetText = "${goalCarbs.toInt()}G",
                targetSubtext = "Углеводов осталось",
                leftText = "",
                pieData = ratios(totalCarbs, goalCarbs),
            ),
        )
    }
}

fun ratios(consumed: Double, goal: Double): List<Float> {
    if (goal <= 0.0) return listOf(0f, 0f)

    val consumedRatio = (consumed / goal)
        .coerceIn(0.0, 1.0)

    return listOf(
        consumedRatio.toFloat(),
        (1.0 - consumedRatio).toFloat()
    )
}
