package dev.calorai.mobile.features.meal.data.dto.mealEntry

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealEntryRecognizeResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String,

    @SerialName("brand")
    val brand: String,

    @SerialName("barcode")
    val barcode: String,

    @SerialName("provider")
    val provider: String,

    @SerialName("kcalPer100g")
    val kcalPer100g: Double,

    @SerialName("proteinPer100g")
    val proteinPer100g: Double,

    @SerialName("fatPer100g")
    val fatPer100g: Double,

    @SerialName("carbsPer100g")
    val carbsPer100g: Double,
)
