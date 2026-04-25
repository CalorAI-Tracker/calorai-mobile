package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealEntryPayload

interface CreateMealEntryUseCase {

    suspend operator fun invoke(payload: MealEntryPayload)
}

class CreateMealEntryUseCaseImpl(
    private val repository: MealRepository,
) : CreateMealEntryUseCase {

    override suspend fun invoke(payload: MealEntryPayload) {
        repository.createMealEntryAndSync(payload)
    }
}
