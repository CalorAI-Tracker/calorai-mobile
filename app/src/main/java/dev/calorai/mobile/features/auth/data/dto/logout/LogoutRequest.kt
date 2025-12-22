package dev.calorai.mobile.features.auth.data.dto.logout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(

    @SerialName("refreshToken")
    val refreshToken: String,
)
