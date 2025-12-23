package dev.calorai.mobile.features.profile.data.dto.updateUser

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileResponse(

    @SerialName("userId")
    val userId: Int,

    @SerialName("name")
    val name: String,

    @SerialName("email")
    val email: String,

    @SerialName("sex")
    val sex: String,

    @SerialName("height")
    val height: Int,

    @SerialName("weight")
    val weight: Int,

    @SerialName("birthDay")
    val birthDay: String,

    @SerialName("activityCode")
    val activityCode: String,

    @SerialName("healthGoalCode")
    val healthGoalCode: String,
)
