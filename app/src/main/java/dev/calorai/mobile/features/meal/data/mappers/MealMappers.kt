package dev.calorai.mobile.features.meal.data.mappers

import dev.calorai.mobile.features.meal.data.dto.createMealEntry.CreateMealEntryRequest
import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.MealDto
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity

fun MealDto.toEntity(date: String): DailyMealsEntity =
    DailyMealsEntity(
        id = 0L,
        date = date,
        meal = this.meal,
        kcal = this.kcal,
        proteinG = this.proteinG,
        fatG = this.fatG,
        carbsG = this.carbsG,
        entriesCnt = this.entriesCnt
    )
