package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.MealType

interface DeleteMealUseCase {

    suspend operator fun invoke(
        date: String,
        mealType: MealType,
    )
}

internal class DeleteMealUseCaseImpl(
    private val repository: MealRepository,
) : DeleteMealUseCase {

    override suspend fun invoke(
        date: String,
        mealType: MealType,
    ) {
        repository.deleteMeal(date, mealType)
    }
}
