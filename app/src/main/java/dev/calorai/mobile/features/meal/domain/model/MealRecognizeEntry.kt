package dev.calorai.mobile.features.meal.domain.model

data class MealRecognizeEntry constructor(
    val name: String,
    val kcalPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
)
