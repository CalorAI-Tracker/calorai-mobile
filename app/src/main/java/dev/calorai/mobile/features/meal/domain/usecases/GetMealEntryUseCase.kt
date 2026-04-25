package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealEntry
import dev.calorai.mobile.features.meal.domain.model.MealEntryId

interface GetMealEntryUseCase {

    suspend operator fun invoke(mealEntryId: MealEntryId): MealEntry
}

class GetMealEntryUseCaseImpl(
    private val repository: MealRepository,
) : GetMealEntryUseCase {

    override suspend fun invoke(mealEntryId: MealEntryId): MealEntry {
        return repository.getMealEntry(mealEntryId)
    }
}
