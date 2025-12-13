package dev.calorai.mobile.features.auth.data.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val deviceId: String,
)
