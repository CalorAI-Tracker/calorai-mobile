package dev.calorai.mobile.features.meal.domain.model

data class MealEntry(
    val name: String,
    val quantityGrams: Double,
    val kcal: Int,
    val proteinG: Double,
    val fatG: Double,
    val carbsG: Double,
    val urlToImage: String? = null,  // TODO: Потом использовать, когда с бека будет приходить
)
