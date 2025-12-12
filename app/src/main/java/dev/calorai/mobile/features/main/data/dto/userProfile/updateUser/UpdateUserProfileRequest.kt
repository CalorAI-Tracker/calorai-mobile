package dev.calorai.mobile.features.main.data.dto.userProfile.updateUser

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    val email: String,
    val name: String,
    val sex: String,
    val height: Int,
    val weight: Int,
    val birthDay: String,
    val activityCode: String,
    val healthGoalCode: String,
)
