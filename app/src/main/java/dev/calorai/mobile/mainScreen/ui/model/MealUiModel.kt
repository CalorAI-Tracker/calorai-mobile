package dev.calorai.mobile.mainScreen.ui.model

data class MealUiModel(
    val id: Long,
    val title: String,
    val subtitle: String,
    val visibleFoodList: List<FoodUiModel>,
)

data class FoodUiModel(
    val id: Long,
    val name: String,
    val urlToImage: String?,
)