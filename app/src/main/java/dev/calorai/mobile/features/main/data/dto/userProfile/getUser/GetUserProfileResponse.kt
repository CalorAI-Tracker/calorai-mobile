package dev.calorai.mobile.features.main.data.dto.userProfile.getUser

import dev.calorai.mobile.features.main.data.dto.userProfile.enums.ActivityCode
import dev.calorai.mobile.features.main.data.dto.userProfile.enums.HealthGoalCode
import dev.calorai.mobile.features.main.data.dto.userProfile.enums.Sex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserProfileResponse(

    @SerialName("userId")
    val userId: Int,

    @SerialName("name")
    val name: String,

    @SerialName("email")
    val email: String,

    @SerialName("sex")
    val sex: Sex,

    @SerialName("height")
    val height: Int,

    @SerialName("weight")
    val weight: Int,

    @SerialName("birthDay")
    val birthDay: String,

    @SerialName("activityCode")
    val activityCode: ActivityCode,

    @SerialName("healthGoalCode")
    val healthGoalCode: HealthGoalCode,
)
