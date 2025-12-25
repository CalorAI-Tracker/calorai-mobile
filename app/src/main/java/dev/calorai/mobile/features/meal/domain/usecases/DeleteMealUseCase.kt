package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository

interface DeleteMealUseCase {

    suspend operator fun invoke(
        id: Long,
    )
}
internal class DeleteMealUseCaseImpl(
    private val repository: MealRepository,
) : DeleteMealUseCase {

    override suspend fun invoke(id: Long) {
        repository.deleteMealById(id)
    }
}