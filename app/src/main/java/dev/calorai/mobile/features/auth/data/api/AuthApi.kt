package dev.calorai.mobile.features.auth.data.api

import dev.calorai.mobile.features.auth.data.dto.login.LoginRequest
import dev.calorai.mobile.features.auth.data.dto.login.LoginResponse
import dev.calorai.mobile.features.auth.data.dto.logout.LogoutRequest
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshRequest
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshResponse
import dev.calorai.mobile.features.auth.data.dto.signup.SignupRequest
import dev.calorai.mobile.features.auth.data.dto.signup.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/signup")
    suspend fun signUp(
        @Body body: SignupRequest
    ): Response<SignupResponse>

    @POST("auth/refresh")
    suspend fun refresh(
        @Body body: RefreshRequest
    ): Response<RefreshResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Body body: LogoutRequest
    ): Response<Unit>

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<LoginResponse>
}
