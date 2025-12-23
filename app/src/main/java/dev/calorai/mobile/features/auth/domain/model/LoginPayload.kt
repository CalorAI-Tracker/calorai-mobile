package dev.calorai.mobile.features.auth.domain.model

data class LoginPayload(
    val email: String,
    val password: String,
    val deviceId: String,
)
