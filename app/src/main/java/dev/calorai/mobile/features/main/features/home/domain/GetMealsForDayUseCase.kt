package dev.calorai.mobile.features.main.features.home.domain

import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
import kotlinx.coroutines.delay
import java.time.LocalDate

interface GetMealsForDayUseCase {
    suspend operator fun invoke(date: LocalDate): List<MealUiModel>
}

internal class GetMealsForDayUseCaseImpl constructor(
) : GetMealsForDayUseCase {

    override suspend fun invoke(date: LocalDate): List<MealUiModel> {
        delay(1000L)
        return listOf(
            MealUiModel(
                id = 1,
                title = "Завтрак от ${date}",
                visibleFoodList = emptyList(),
                subtitle = "345 ккал",
                type = MealType.BREAKFAST,
            ),
            MealUiModel(
                id = 2,
                title = "Обед от ${date}",
                visibleFoodList = emptyList(),
                subtitle = "345 ккал",
                type = MealType.LUNCH,
            )
        )
    }
}
