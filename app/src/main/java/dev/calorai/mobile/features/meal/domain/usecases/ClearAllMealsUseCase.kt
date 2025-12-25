package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository

interface ClearAllMealsUseCase {

    suspend operator fun invoke()
}

internal class ClearAllMealsUseCaseImpl(
    private val repository: MealRepository,
) : ClearAllMealsUseCase {

    override suspend fun invoke() {
        repository.clearAllMeals()
    }
}