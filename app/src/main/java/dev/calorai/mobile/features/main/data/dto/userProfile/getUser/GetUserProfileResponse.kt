package dev.calorai.mobile.features.main.data.dto.userProfile.getUser

import kotlinx.serialization.Serializable

@Serializable
data class GetUserProfileResponse(
    val userId: Int,
    val name: String,
    val email: String,
    val sex: String,
    val height: Int,
    val weight: Int,
    val birthDay: String,
    val activityCode: String,
    val healthGoalCode: String,
)
