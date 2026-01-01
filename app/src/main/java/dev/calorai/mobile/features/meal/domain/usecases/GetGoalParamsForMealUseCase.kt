package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.model.GoalParams
import dev.calorai.mobile.features.meal.domain.model.MealType

interface GetGoalParamsForMealUseCase {
    suspend operator fun invoke(
        date: String,
        mealType: MealType,
    ): GoalParams
}

class GetGoalParamsForMealUseCaseImpl constructor(
) : GetGoalParamsForMealUseCase {

    override suspend fun invoke(
        date: String,
        mealType: MealType
    ): GoalParams {
        return GoalParams(
            goalKcal = 3600f / 4,
            goalProtein = 222f / 4,
            goalFat = 100f / 4,
            goalCarbs = 490f / 4,
        ) // TODO
    }
}
