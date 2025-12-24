package dev.calorai.mobile.features.profile.domain.model

data class CreateUserProfilePayload(
    val userId: UserId,
    val gender: Gender,
    val height: Int,
    val weight: Int,
    val birthDay: String,
    val name: String,
    val activityCode: Activity,
    val healthGoalCode: Goal,
)
