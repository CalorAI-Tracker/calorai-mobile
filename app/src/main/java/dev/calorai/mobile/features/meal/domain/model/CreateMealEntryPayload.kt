package dev.calorai.mobile.features.meal.domain.model

data class CreateMealEntryPayload(
    val entryName: String,
    val meal: MealType,
    val eatenAt: String,
    val proteinPerBaseG: Double,
    val fatPerBaseG: Double,
    val carbsPerBaseG: Double,
    val baseQuantityGrams: Double,
    val portionQuantityGrams: Double,
    val brand: String = "",
    val barcode: String = "",
    val note: String = "",
)