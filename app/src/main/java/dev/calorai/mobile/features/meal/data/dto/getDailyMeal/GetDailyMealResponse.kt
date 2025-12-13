package dev.calorai.mobile.features.meal.data.dto.getDailyMeal

import kotlinx.serialization.Serializable

@Serializable
data class GetDailyMealResponse(
    val date: String,
    val meals: List<MealDto>,
)

@Serializable
data class MealDto(
    val meal: String,
    val kcal: Int,
    val proteinG: String,
    val fatG: String,
    val carbsG: String,
    val entriesCnt: Int,
)
