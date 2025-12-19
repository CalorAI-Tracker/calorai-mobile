package dev.calorai.mobile.features.main.data.dto.userProfile.createUser

import dev.calorai.mobile.features.main.data.dto.userProfile.enums.ActivityCode
import dev.calorai.mobile.features.main.data.dto.userProfile.enums.HealthGoalCode
import dev.calorai.mobile.features.main.data.dto.userProfile.enums.Sex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserProfileRequest(

    @SerialName("userId")
    val userId: Int,

    @SerialName("sex")
    val sex: Sex,

    @SerialName("height")
    val height: Int,

    @SerialName("weight")
    val weight: Int,

    @SerialName("birthDay")
    val birthDay: String,

    @SerialName("name")
    val name: String,

    @SerialName("activityCode")
    val activityCode: ActivityCode,

    @SerialName("healthGoalCode")
    val healthGoalCode: HealthGoalCode,
)
