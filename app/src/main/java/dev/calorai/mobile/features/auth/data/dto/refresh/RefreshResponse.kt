package dev.calorai.mobile.features.auth.data.dto.refresh

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(

    @SerialName("accessToken")
    val accessToken: String,

    @SerialName("refreshToken")
    val refreshToken: String,

    @SerialName("tokenType")
    val tokenType: String,

    @SerialName("expiresInSeconds")
    val expiresInSeconds: Int,
)
