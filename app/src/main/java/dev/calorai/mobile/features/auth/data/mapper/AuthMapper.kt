package dev.calorai.mobile.features.auth.data.mapper

import dev.calorai.mobile.features.auth.data.dto.login.LoginRequest
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshRequest
import dev.calorai.mobile.features.auth.data.dto.signup.SignupRequest
import dev.calorai.mobile.features.auth.domain.model.LoginPayload
import dev.calorai.mobile.features.auth.domain.model.SignupPayload

class AuthMapper {

    fun mapToRequest(payload: SignupPayload): SignupRequest =
        SignupRequest(
            email = payload.email,
            password = payload.password
        )

    fun mapToRequest(payload: LoginPayload): LoginRequest =
        LoginRequest(
            email = payload.email,
            password = payload.password,
            deviceId = payload.deviceId
        )

    fun mapToRequest(refreshToken: String): RefreshRequest =
        RefreshRequest(refreshToken = refreshToken)
}
