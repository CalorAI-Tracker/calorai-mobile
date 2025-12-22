package dev.calorai.mobile.features.auth.domain

import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.login.LoginRequest
import dev.calorai.mobile.features.auth.data.dto.login.LoginResponse
import dev.calorai.mobile.features.auth.data.dto.logout.LogoutRequest
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshRequest
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshResponse
import dev.calorai.mobile.features.auth.data.dto.signup.SignupRequest
import dev.calorai.mobile.features.auth.data.dto.signup.SignupResponse
import dev.calorai.mobile.features.auth.data.token.TokenStorage
import dev.calorai.mobile.features.main.data.dao.UserDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface AuthRepository {

    suspend fun signUp(request: SignupRequest): Response<SignupResponse>
    suspend fun login(request: LoginRequest): Response<LoginResponse>
    suspend fun refreshToken(request: RefreshRequest): Response<RefreshResponse>
    suspend fun logout(): Response<Unit>
}

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage,
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {

    override suspend fun signUp(request: SignupRequest): Response<SignupResponse> =
        withContext(dispatcher) {
            api.signUp(request)
        }

    override suspend fun login(request: LoginRequest): Response<LoginResponse> =
        withContext(dispatcher) {
            val response = api.login(request)
            if (response.isSuccessful) {
                val body = response.body()
                    ?: throw IllegalStateException("Login response is successful but body is null")
                tokenStorage.setTokens(body.accessToken, body.refreshToken)
            }
            response
        }

    override suspend fun refreshToken(request: RefreshRequest): Response<RefreshResponse> =
        withContext(dispatcher) {
            val response = api.refresh(request)
            if (response.isSuccessful) {
                val body = response.body()
                    ?: throw IllegalStateException("Refresh response is successful but body is null")
                tokenStorage.setTokens(body.accessToken, body.refreshToken)
            }
            response
        }

    override suspend fun logout(): Response<Unit> =
        withContext(dispatcher) {
            val refresh = tokenStorage.getRefreshToken()
                ?: throw IllegalStateException("No refresh token available for logout")
            val response = api.logout(LogoutRequest(refresh))
            if (response.isSuccessful) {
                tokenStorage.clearTokens()
                userDao.clear()
            }
            response
        }
}
