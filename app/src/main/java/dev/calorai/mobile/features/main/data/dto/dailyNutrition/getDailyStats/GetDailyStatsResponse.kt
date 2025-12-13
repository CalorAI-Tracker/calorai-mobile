package dev.calorai.mobile.features.main.data.dto.dailyNutrition.getDailyStats

import kotlinx.serialization.Serializable

@Serializable
data class GetDailyStatsResponse(
    val date: String,
    val plan: PlanDto,
    val actual: ActualDto,
    val remaining: RemainingDto,
    val completionPercent: CompletionPercentDto
)

@Serializable
data class PlanDto(
    val kcal: Int,
    val proteinG: Double,
    val fatG: Double,
    val carbsG: Double
)

@Serializable
data class ActualDto(
    val kcal: Int,
    val proteinG: Double,
    val fatG: Double,
    val carbsG: Double
)

@Serializable
data class RemainingDto(
    val kcal: Int,
    val proteinG: Double,
    val fatG: Double,
    val carbsG: Double
)

@Serializable
data class CompletionPercentDto(
    val kcal: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int
)
