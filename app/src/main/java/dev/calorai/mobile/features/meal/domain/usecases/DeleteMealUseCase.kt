package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealId

interface DeleteMealUseCase {

    suspend operator fun invoke(
        id: MealId,
    )
}
internal class DeleteMealUseCaseImpl(
    private val repository: MealRepository,
) : DeleteMealUseCase {

    override suspend fun invoke(id: MealId) {
        repository.deleteMealById(id)
    }
}