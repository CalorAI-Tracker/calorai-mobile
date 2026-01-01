package dev.calorai.mobile.features.meal.domain.model

data class MealProgressInfo constructor(
    val entries: List<MealEntry>,
    val remainingAmountProtein: Float,
    val remainingAmountFat: Float,
    val remainingAmountCarbs: Float,
    val ratioProtein: List<Float>,
    val ratioFat: List<Float>,
    val ratioCarbs: List<Float>,
)
