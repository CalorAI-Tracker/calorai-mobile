package dev.calorai.mobile.features.main.features.plan.data.dto.getActivityLevels

import kotlinx.serialization.Serializable

@Serializable
data class GetActivityLevelsResponse(
    val id: Int,
    val code: String,
    val title: String,
    val description: String,
    val factor: Int
)
