package dev.calorai.mobile.features.auth.domain.model

import dev.calorai.mobile.core.local.deviceId.DeviceId

data class LoginPayload(
    val email: String,
    val password: String,
    val deviceId: DeviceId,
)
