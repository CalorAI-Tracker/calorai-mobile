package dev.calorai.mobile.features.auth.domain

import dev.calorai.mobile.features.auth.domain.model.LoginPayload
import dev.calorai.mobile.features.auth.domain.model.SignupPayload
import dev.calorai.mobile.features.profile.domain.model.UserId

interface AuthRepository {

    suspend fun signUp(payload: SignupPayload): UserId
    suspend fun login(payload: LoginPayload): UserId
    suspend fun refreshToken(refreshToken: String)
    suspend fun logout()
}
