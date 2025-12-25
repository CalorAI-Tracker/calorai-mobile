package dev.calorai.mobile.features.home.domain.usecases

import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.DailyMeal

interface GetDailyMealsUseCase {

    suspend operator fun invoke(
        date: String,
    ) : List<MealUiModel>
}

internal class GetDailyMealsUseCaseImpl(
    private val repository: MealRepository
) : GetDailyMealsUseCase {

    override suspend fun invoke(
        date: String
    ) : List<MealUiModel> {

        val dailyMeals: List<DailyMeal> = repository.getDailyMeals(date)
        return dailyMeals.map { dailyMeal ->
            MealUiModel(
                id = dailyMeal.id,
                title = mapMealTitle(dailyMeal.meal),
                subtitle = "${dailyMeal.kcal} ккал",
                visibleFoodList = emptyList(),
                type = mapMealType(dailyMeal.meal),
            )
        }
    }
}

private fun mapMealType(meal: String): MealType =
    when (meal) {
        "BREAKFAST" -> MealType.BREAKFAST
        "LUNCH" -> MealType.LUNCH
        "DINNER" -> MealType.DINNER
        "SNACK" -> MealType.SNACK
        else -> error("Unknown meal type: $meal")
    }

private fun mapMealTitle(meal: String): String =
    when (meal) {
        "BREAKFAST" -> "Завтрак"
        "LUNCH" -> "Обед"
        "DINNER" -> "Ужин"
        "SNACK" -> "Перекус"
        else -> error("Unknown meal title: $meal")
    }
