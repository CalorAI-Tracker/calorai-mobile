package dev.calorai.mobile.features.meal.data.dto.createMealEntry

import dev.calorai.mobile.features.meal.data.dto.enums.MealTypeDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMealEntryRequest(

    @SerialName("entryName")
    val entryName: String,

    @SerialName("meal")
    val meal: MealTypeDto,

    @SerialName("eatenAt")
    val eatenAt: String,

    @SerialName("proteinPerBaseG")
    val proteinPerBaseG: Double,

    @SerialName("fatPerBaseG")
    val fatPerBaseG: Double,

    @SerialName("carbsPerBaseG")
    val carbsPerBaseG: Double,

    @SerialName("baseQuantityGrams")
    val baseQuantityGrams: Double,

    @SerialName("portionQuantityGrams")
    val portionQuantityGrams: Double,

    @SerialName("brand")
    val brand: String,

    @SerialName("barcode")
    val barcode: String,

    @SerialName("note")
    val note: String,
)
