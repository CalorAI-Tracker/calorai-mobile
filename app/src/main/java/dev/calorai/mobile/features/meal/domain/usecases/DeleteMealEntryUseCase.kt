package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealEntryId

interface DeleteMealEntryUseCase {

    suspend operator fun invoke(mealEntryId: MealEntryId, date: String)
}

class DeleteMealEntryUseCaseImpl(
    private val repository: MealRepository,
) : DeleteMealEntryUseCase {

    override suspend fun invoke(mealEntryId: MealEntryId, date: String) {
        repository.deleteMealEntryAndSync(mealEntryId, date)
    }
}
