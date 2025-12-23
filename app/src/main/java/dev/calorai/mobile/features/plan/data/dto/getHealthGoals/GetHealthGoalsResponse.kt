package dev.calorai.mobile.features.plan.data.dto.getHealthGoals

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetHealthGoalsResponse(

    @SerialName("id")
    val id: Int,

    @SerialName("code")
    val code: String,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String,

    @SerialName("calorieDeltaPercent")
    val calorieDeltaPercent: Int,
)
