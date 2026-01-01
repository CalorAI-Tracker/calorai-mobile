package dev.calorai.mobile.features.meal.data.dto.getDailyMealsComposition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDailyMealsCompositionResponse(

    @SerialName("date")
    val date: String,

    @SerialName("meals")
    val meals: Map<String, List<MealEntryDto>>
)

@Serializable
data class MealEntryDto(
    @SerialName("entryName")
    val entryName: String,

    @SerialName("quantityGrams")
    val quantityGrams: Double,

    @SerialName("kcal")
    val kcal: Int,

    @SerialName("proteinG")
    val proteinG: Double,

    @SerialName("fatG")
    val fatG: Double,

    @SerialName("carbsG")
    val carbsG: Double
)
