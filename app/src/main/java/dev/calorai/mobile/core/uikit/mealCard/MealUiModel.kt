package dev.calorai.mobile.core.uikit.mealCard

import dev.calorai.mobile.features.home.ui.model.MealTypeUi
import dev.calorai.mobile.features.meal.domain.model.MealType

data class MealUiModel constructor(
    val id: Long,
    val title: MealTypeUi,
    val subtitleValue: Int,
    val visibleFoodList: List<FoodUiModel>,
    val type: MealType
)

data class FoodUiModel(
    val id: Long,
    val name: String,
    val urlToImage: String?,
)