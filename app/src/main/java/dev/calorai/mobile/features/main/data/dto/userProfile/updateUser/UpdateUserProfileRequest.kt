package dev.calorai.mobile.features.main.data.dto.userProfile.updateUser

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(

    @SerialName("email")
    val email: String,

    @SerialName("name")
    val name: String,

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
