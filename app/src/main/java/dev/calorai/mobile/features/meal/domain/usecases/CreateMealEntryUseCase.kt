package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.CreateMealEntryPayload

interface CreateMealEntryUseCase {

    suspend operator fun invoke(payload: CreateMealEntryPayload)
}

class CreateMealEntryUseCaseImpl(
    private val repository: MealRepository,
) : CreateMealEntryUseCase {

    override suspend fun invoke(payload: CreateMealEntryPayload) {
        repository.createMealEntryAndSync(payload)
    }
}