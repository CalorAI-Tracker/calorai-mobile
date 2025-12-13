package dev.calorai.mobile.features.main.features.plan.data.dto.getHealthGoals

import kotlinx.serialization.Serializable

@Serializable
data class GetHealthGoalsResponse(
    val id: Int,
    val code: String,
    val title: String,
    val description: String,
    val calorieDeltaPercent: Int,
)
