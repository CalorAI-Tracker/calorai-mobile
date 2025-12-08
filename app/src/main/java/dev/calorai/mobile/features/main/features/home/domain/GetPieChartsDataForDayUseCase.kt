package dev.calorai.mobile.features.main.features.home.domain

import dev.calorai.mobile.core.uikit.pieChart.PieChartUiModel
import kotlinx.coroutines.delay
import java.time.LocalDate

interface GetPieChartsDataForDayUseCase {
    suspend operator fun invoke(date: LocalDate): List<PieChartUiModel>
}

internal class GetPieChartsDataForDayUseCaseImpl(
) : GetPieChartsDataForDayUseCase {

    override suspend fun invoke(date: LocalDate): List<PieChartUiModel> {
        delay(1000L)
        return listOf(
            PieChartUiModel(
                targetText = "1003",
                targetSubtext = "ккал осталось",
                leftText = "325 ккал",
                pieData = listOf(40f, 60f)
            ),
            PieChartUiModel(
                targetText = "59 г",
                targetSubtext = "белка\nосталось",
                leftText = "325 ккал",
                pieData = listOf(50f, 50f)
            ),
            PieChartUiModel(
                targetText = "149 г",
                targetSubtext = "Углеводов\nосталось",
                leftText = "24 ккал",
                pieData = listOf(30f, 70f)
            ),
            PieChartUiModel(
                targetText = "23 г",
                targetSubtext = "Жиров\nосталось",
                leftText = "325 ккал",
                pieData = listOf(20f, 80f)
            ),
        )
    }
}