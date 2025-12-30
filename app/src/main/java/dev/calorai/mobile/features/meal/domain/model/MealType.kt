package dev.calorai.mobile.features.meal.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
}
