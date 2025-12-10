package dev.calorai.mobile.features.meal.create.ui

data class CreateMealManualUiState(
    val name: String = "",
    val calories: Double = 0.0,
    val proteins: Double = 0.0,
    val fats: Double = 0.0,
    val carbs: Double = 0.0,
    val portion: Double = 0.0,
)