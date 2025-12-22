package dev.calorai.mobile.features.meal.data.dto.getDailyMeal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDailyMealResponse(

    @SerialName("date")
    val date: String,

    @SerialName("meals")
    val meals: List<MealDto>,
)

@Serializable
data class MealDto(

    @SerialName("meal")
    val meal: String,

    @SerialName("kcal")
    val kcal: Int,

    @SerialName("proteinG")
    val proteinG: String,

    @SerialName("fatG")
    val fatG: String,

    @SerialName("carbsG")
    val carbsG: String,

    @SerialName("entriesCnt")
    val entriesCnt: Int,
)
