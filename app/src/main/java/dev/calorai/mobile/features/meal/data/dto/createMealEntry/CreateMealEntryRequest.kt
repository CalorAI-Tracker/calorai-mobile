package dev.calorai.mobile.features.meal.data.dto.createMealEntry

import kotlinx.serialization.Serializable

@Serializable
data class CreateMealEntryRequest(
    val entryName: String,
    val meal: String,
    val eatenAt: String,
    val proteinPerBaseG: Double,
    val fatPerBaseG: Double,
    val carbsPerBaseG: Double,
    val baseQuantityGrams: Double,
    val portionQuantityGrams: Double,
    val brand: String,
    val barcode: String,
    val note: String,
)
