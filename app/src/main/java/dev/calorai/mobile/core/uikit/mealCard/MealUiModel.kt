package dev.calorai.mobile.core.uikit.mealCard

data class MealUiModel constructor(
    val id: Long,
    val title: String,
    val subtitle: String,
    val visibleFoodList: List<FoodUiModel>,
    val type: MealType,
)

data class FoodUiModel(
    val id: Long,
    val name: String,
    val urlToImage: String?,
)