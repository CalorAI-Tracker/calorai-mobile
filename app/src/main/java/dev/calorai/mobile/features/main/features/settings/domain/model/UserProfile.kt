package dev.calorai.mobile.features.main.features.settings.domain.model

data class UserProfile(
    val userId: Long,
    val name: String,
    val email: String,
    val gender: Gender,
    val height: Int,
    val weight: Int,
    val birthDay: String,
    val activityCode: Activity,
    val healthGoalCode: Goal,
    val targetWeightKg: Int? = null,
    val weeklyRateKg: Int? = null,
)
