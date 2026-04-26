package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealEntry

interface GetAllMealEntriesUseCase {
    suspend operator fun invoke(): List<MealEntry>
}

class GetAllMealEntriesUseCaseImpl(
    private val repository: MealRepository,
) : GetAllMealEntriesUseCase {

    override suspend fun invoke(): List<MealEntry> = repository.getAllMealEntries()
}
