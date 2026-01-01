package dev.calorai.mobile.features.home.domain.usecases

import dev.calorai.mobile.features.meal.domain.model.GoalParams
import java.time.LocalDate

interface GetGoalParamsForDayUseCase {
    suspend operator fun invoke(
        date: LocalDate,
    ): GoalParams
}

class GetGoalParamsForDayUseCaseImpl constructor(
) : GetGoalParamsForDayUseCase {

    override suspend fun invoke(
        date: LocalDate,
    ): GoalParams {
        return GoalParams(
            goalKcal = 3600f,
            goalProtein = 222f,
            goalFat = 100f,
            goalCarbs = 490f,
        ) // TODO
    }
}
