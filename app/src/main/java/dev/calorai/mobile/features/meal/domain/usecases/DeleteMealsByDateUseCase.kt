package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository

interface DeleteMealsByDateUseCase {

    suspend operator fun invoke(
        date: String,
    )
}

internal class DeleteMealsByDateUseCaseImpl(
    private val repository: MealRepository,
) : DeleteMealsByDateUseCase{

    override suspend fun invoke(date: String) {
        repository.deleteMealsByDate(date)
    }
}