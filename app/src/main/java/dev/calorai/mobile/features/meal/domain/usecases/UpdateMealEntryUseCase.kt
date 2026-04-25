package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealEntryId
import dev.calorai.mobile.features.meal.domain.model.MealEntryPayload

interface UpdateMealEntryUseCase {

    suspend operator fun invoke(mealEntryId: MealEntryId, payload: MealEntryPayload)
}

class UpdateMealEntryUseCaseImpl constructor(
    private val repository: MealRepository,
) : UpdateMealEntryUseCase {

    override suspend fun invoke(mealEntryId: MealEntryId, payload: MealEntryPayload) {
        repository.updateMealEntryAndSync(mealEntryId, payload)
    }
}
