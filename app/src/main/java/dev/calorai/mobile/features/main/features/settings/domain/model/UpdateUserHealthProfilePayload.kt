package dev.calorai.mobile.features.main.features.settings.domain.model

data class UpdateUserHealthProfilePayload(
    val sex: String,
    val height: Long,
    val weight: Long,
    val birthDay: String,
    val activityCode: String,
    val healthGoalCode: String,
    val targetWeightKg: Long? = null,
    val weeklyRateKg: Long? = null,
)
