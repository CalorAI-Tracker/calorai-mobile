package dev.calorai.mobile.features.auth.data.dto.signup

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    val email: String,
    val id: Int
)