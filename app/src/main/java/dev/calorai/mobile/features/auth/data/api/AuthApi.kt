package dev.calorai.mobile.features.auth.data.api

import dev.calorai.mobile.features.auth.data.dto.login.LoginRequest
import dev.calorai.mobile.features.auth.data.dto.login.LoginResponse
import dev.calorai.mobile.features.auth.data.dto.logout.LogoutRequest
import dev.calorai.mobile.features.auth.data.dto.logout.LogoutResponse
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshRequest
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshResponse
import dev.calorai.mobile.features.auth.data.dto.signup.SignupDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/signup")
    suspend fun signUp(
        @Body body: SignupDto
    ): SignupDto

    @POST("auth/refresh")
    suspend fun refresh(
        @Body body: RefreshRequest
    ): RefreshResponse

    @POST("auth/logout")
    suspend fun logout(
        @Body body: LogoutRequest
    ): LogoutResponse

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): LoginResponse
}