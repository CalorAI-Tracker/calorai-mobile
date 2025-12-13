package dev.calorai.mobile.features.auth.data.dto.signup

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val email: String,
    val password: String
)
