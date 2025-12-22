package dev.calorai.mobile.features.main.features.plan.data.dto.getActivityLevels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetActivityLevelsResponse(

    @SerialName("id")
    val id: Int,

    @SerialName("code")
    val code: String,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String,

    @SerialName("factor")
    val factor: Int
)
