package dev.calorai.mobile.features.auth.data.dto.signup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(

    @SerialName("email")
    val email: String,

    @SerialName("id")
    val id: Int
)
