package dev.calorai.mobile.features.auth.data.dto.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(

    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String,

    @SerialName("deviceId")
    val deviceId: String,
)
