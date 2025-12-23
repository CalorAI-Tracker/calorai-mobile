package dev.calorai.mobile.features.auth.domain

import dev.calorai.mobile.features.auth.domain.model.LoginPayload
import dev.calorai.mobile.features.auth.domain.model.SignupPayload

interface AuthRepository {

    suspend fun signUp(payload: SignupPayload): Int
    suspend fun login(payload: LoginPayload)
    suspend fun refreshToken(refreshToken: String)
    suspend fun logout()
}
