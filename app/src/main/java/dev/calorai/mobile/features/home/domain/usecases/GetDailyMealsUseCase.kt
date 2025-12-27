package dev.calorai.mobile.features.home.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.DailyMeal

interface GetMealsForDayUseCase {

    suspend operator fun invoke(
        date: String,
    ) : List<DailyMeal>
}

internal class GetMealsForDayUseCaseImpl(
    private val repository: MealRepository
) : GetMealsForDayUseCase {

    override suspend fun invoke(
        date: String
    ) : List<DailyMeal> {
        return repository.getDailyMeals(date)
    }
}
