package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.CreateMealEntryPayload
import dev.calorai.mobile.features.meal.domain.model.MealEntry
import dev.calorai.mobile.features.meal.domain.model.MealType
import java.time.LocalDate

interface GetMealIngredientsUseCase {

    suspend operator fun invoke(
        date: String,
        mealType: MealType,
    ): List<MealEntry>
}

class GetMealIngredientsUseCaseImpl(
    private val repository: MealRepository,
) : GetMealIngredientsUseCase {

    override suspend fun invoke(
        date: String,
        mealType: MealType,
    ): List<MealEntry> {
        return repository.getMealIngredients(
            date = date,
            mealType = mealType,
        )
    }
}
