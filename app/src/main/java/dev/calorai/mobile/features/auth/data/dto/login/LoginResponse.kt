package dev.calorai.mobile.features.auth.data.dto.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(

    @SerialName("id")
    val id: Long,

    @SerialName("accessToken")
    val accessToken: String,

    @SerialName("refreshToken")
    val refreshToken: String,

    @SerialName("tokenType")
    val tokenType: String,

    @SerialName("expiresInSeconds")
    val expiresInSeconds: Int
)
