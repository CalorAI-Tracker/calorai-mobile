package dev.calorai.mobile.features.home.data.dto.dailyNutrition.getDailyStats

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDailyStatsResponse(

    @SerialName("date")
    val date: String,

    @SerialName("plan")
    val plan: PlanDto,

    @SerialName("actual")
    val actual: ActualDto,

    @SerialName("remaining")
    val remaining: RemainingDto,

    @SerialName("completionPercent")
    val completionPercent: CompletionPercentDto
)

@Serializable
data class PlanDto(

    @SerialName("kcal")
    val kcal: Int,

    @SerialName("proteinG")
    val proteinG: Double,

    @SerialName("fatG")
    val fatG: Double,

    @SerialName("carbsG")
    val carbsG: Double
)

@Serializable
data class ActualDto(

    @SerialName("kcal")
    val kcal: Int,

    @SerialName("proteinG")
    val proteinG: Double,

    @SerialName("fatG")
    val fatG: Double,

    @SerialName("carbsG")
    val carbsG: Double
)

@Serializable
data class RemainingDto(

    @SerialName("kcal")
    val kcal: Int,

    @SerialName("proteinG")
    val proteinG: Double,

    @SerialName("fatG")
    val fatG: Double,

    @SerialName("carbsG")
    val carbsG: Double
)

@Serializable
data class CompletionPercentDto(

    @SerialName("kcal")
    val kcal: Int,

    @SerialName("protein")
    val protein: Int,

    @SerialName("fat")
    val fat: Int,

    @SerialName("carbs")
    val carbs: Int
)
