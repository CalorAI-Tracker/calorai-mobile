package dev.calorai.mobile.features.meal.domain.model

import java.time.LocalDate

data class DailyMeal(

    val id: MealId,
    val date: LocalDate,
    val meal: MealType,
    val kcal: Int,
    val proteinG: String,
    val fatG: String,
    val carbsG: String,
    val entriesCnt: Int,
)
