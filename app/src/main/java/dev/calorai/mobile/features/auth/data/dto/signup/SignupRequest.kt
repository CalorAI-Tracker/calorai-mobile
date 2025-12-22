package dev.calorai.mobile.features.auth.data.dto.signup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(

    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String
)
