package dev.calorai.mobile.features.main.data.dto.userProfile.createUser

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserProfileRequest(
    val userId: Int,
    val sex: String,
    val height: Int,
    val weight: Int,
    val birthDay: String,
    val name: String,
    val activityCode: String,
    val healthGoalCode: String,
)
