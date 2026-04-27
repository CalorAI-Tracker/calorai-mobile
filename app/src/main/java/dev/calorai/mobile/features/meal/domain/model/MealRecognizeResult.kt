package dev.calorai.mobile.features.meal.domain.model

sealed interface MealRecognizeResult {
    data class Success(val entry: MealRecognizeEntry) : MealRecognizeResult
    data object NotDetected : MealRecognizeResult
}

data class MealRecognizeEntry constructor(
    val name: String,
    val kcalPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
)
